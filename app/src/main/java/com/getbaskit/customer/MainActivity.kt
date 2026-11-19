
package com.getbaskit.customer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WEB_CLIENT_ID =
            "28116496594-8675ig3qfb0ea5m4liicmqe35e4s05ie.apps.googleusercontent.com"

        private const val WEBSITE_ORIGIN =
            "https://getbaskit.netlify.app"

        private const val GPS_TAG = "BASKIT_GPS"
    }

    private lateinit var webView: WebView
    private lateinit var loadingProgress: ProgressBar
    private lateinit var loadingScreen: View

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private var pendingGeoCallback: GeolocationPermissions.Callback? = null
    private var pendingGeoOrigin: String? = null

    private var googleSignInInProgress = false

    private val filePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            fileUploadCallback?.onReceiveValue(
                if (uri != null) arrayOf(uri) else null
            )
            fileUploadCallback = null
        }

    private val locationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                        hasLocationPermission()

            val callback = pendingGeoCallback
            val origin = pendingGeoOrigin

            pendingGeoCallback = null
            pendingGeoOrigin = null

            Log.d(
                GPS_TAG,
                "Android permission result: granted=$granted, origin=$origin"
            )

            if (granted && !origin.isNullOrBlank()) {
                GeolocationPermissions.getInstance().allow(origin)
            }

            callback?.invoke(origin, granted, granted)

            if (!granted) {
                Toast.makeText(
                    this,
                    "Location permission denied. Please enable location access in app settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("BASKIT_STARTUP", "Updated DailyCart MainActivity is running")

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        loadingProgress = findViewById(R.id.loadingProgress)
        loadingScreen = findViewById(R.id.loadingScreen)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            setGeolocationEnabled(true)
            allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(false)

            userAgentString = "$userAgentString BaskitAndroidApp"
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                val url = request?.url ?: return false

                if (
                    url.scheme == "baskit" &&
                    url.host == "google-sign-in"
                ) {
                    startNativeGoogleSignIn()
                    return true
                }

                return !(
                        url.scheme == "https" &&
                                url.host == "getbaskit.netlify.app"
                        )
            }

            override fun onPageFinished(
                view: WebView?,
                url: String?
            ) {
                super.onPageFinished(view, url)

                loadingScreen.visibility = View.GONE
                loadingProgress.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(
                view: WebView?,
                progress: Int
            ) {
                if (loadingScreen.visibility == View.VISIBLE) {
                    loadingProgress.visibility =
                        if (progress < 100) View.VISIBLE else View.GONE
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {

                fileUploadCallback?.onReceiveValue(null)
                fileUploadCallback = filePathCallback

                filePicker.launch("*/*")
                return true
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {

                val parsedOrigin = Uri.parse(origin ?: "")

                val isTrustedOrigin =
                    parsedOrigin.scheme == "https" &&
                            parsedOrigin.host == "getbaskit.netlify.app"

                val hasPermission = hasLocationPermission()

                Log.d(
                    GPS_TAG,
                    "Geolocation request: origin=$origin, trusted=$isTrustedOrigin, permission=$hasPermission"
                )

                if (!isTrustedOrigin || origin.isNullOrBlank()) {
                    Log.e(GPS_TAG, "Rejected untrusted origin: $origin")
                    callback?.invoke(origin, false, false)
                    return
                }

                if (hasPermission) {
                    Log.d(GPS_TAG, "Android location permission already granted")

                    GeolocationPermissions.getInstance().allow(origin)
                    callback?.invoke(origin, true, true)

                    return
                }

                Log.d(GPS_TAG, "Requesting Android location permission")

                pendingGeoCallback?.invoke(
                    pendingGeoOrigin,
                    false,
                    false
                )

                pendingGeoCallback = callback
                pendingGeoOrigin = origin

                locationPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        webView.visibility = View.INVISIBLE
        loadingScreen.visibility = View.VISIBLE
        loadingProgress.visibility = View.VISIBLE

        // Load DailyCart without clearing saved geolocation permissions.
        webView.loadUrl("$WEBSITE_ORIGIN/")

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    private fun startNativeGoogleSignIn() {

        if (googleSignInInProgress) return

        googleSignInInProgress = true

        lifecycleScope.launch {
            try {
                val idToken = getGoogleIdToken()

                sendGoogleSignInResult(
                    idToken = idToken,
                    error = null
                )

            } catch (error: Exception) {

                val message =
                    error.message ?: "Google sign-in failed."

                sendGoogleSignInResult(
                    idToken = null,
                    error = message
                )

                Toast.makeText(
                    this@MainActivity,
                    message,
                    Toast.LENGTH_LONG
                ).show()

            } finally {
                googleSignInInProgress = false
            }
        }
    }

    private suspend fun getGoogleIdToken(): String {

        val credentialManager = CredentialManager.create(this)

        val authorizedOption =
            GetGoogleIdOption.Builder()
                .setServerClientId(WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(true)
                .build()

        val authorizedRequest =
            GetCredentialRequest.Builder()
                .addCredentialOption(authorizedOption)
                .build()

        val result = try {

            credentialManager.getCredential(
                context = this,
                request = authorizedRequest
            )

        } catch (error: NoCredentialException) {

            val allAccountsOption =
                GetGoogleIdOption.Builder()
                    .setServerClientId(WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()

            val allAccountsRequest =
                GetCredentialRequest.Builder()
                    .addCredentialOption(allAccountsOption)
                    .build()

            credentialManager.getCredential(
                context = this,
                request = allAccountsRequest
            )
        }

        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            val googleCredential =
                GoogleIdTokenCredential.createFrom(
                    credential.data
                )

            return googleCredential.idToken
        }

        throw IllegalStateException(
            "Google did not return a valid ID token."
        )
    }

    private fun sendGoogleSignInResult(
        idToken: String?,
        error: String?
    ) {

        val tokenArgument =
            idToken?.let { JSONObject.quote(it) } ?: "null"

        val errorArgument =
            error?.let { JSONObject.quote(it) } ?: "null"

        runOnUiThread {
            if (isFinishing || isDestroyed) return@runOnUiThread

            webView.evaluateJavascript(
                """
                if (window.onNativeGoogleSignInResult) {
                    window.onNativeGoogleSignInResult(
                        $tokenArgument,
                        $errorArgument
                    );
                }
                """.trimIndent(),
                null
            )
        }
    }

    override fun onDestroy() {

        fileUploadCallback?.onReceiveValue(null)
        fileUploadCallback = null

        pendingGeoCallback?.invoke(
            pendingGeoOrigin,
            false,
            false
        )

        pendingGeoCallback = null
        pendingGeoOrigin = null

        webView.stopLoading()
        webView.destroy()

        super.onDestroy()
    }
}
