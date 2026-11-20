# 🛒 DailyCart – Android App

**Freshness at Your Doorstep.**

DailyCart is an Android application for the DailyCart grocery delivery platform, providing a convenient grocery shopping experience through Android WebView integration and native Android functionality.

## 📸 App Preview

![DailyCart Android App Preview](screenshots/baskit-android-preview.png)

## ✨ Features

- 🛍️ Online grocery shopping
- 🔐 Firebase Authentication
- 🔑 Google Sign-In
- 📧 Email and password authentication
- 🚀 Custom branded loading screen
- 🌐 WebView integration
- 📍 Location access
- 📷 Image and file upload support
- 🔄 Native Android navigation
- 👤 Firebase role-based authentication

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Android development |
| XML | User interface |
| Android Studio | Development environment |
| Android WebView | Web integration |
| Firebase Authentication | User authentication |
| Android Credential Manager | Google Sign-In |
| AndroidX | Android compatibility |
| Material Design | UI components |

## 📱 Application Information

| Property | Details |
|---|---|
| Application Name | DailyCart |
| Package Name | `com.getbaskit.customer` |
| Language | Kotlin |
| UI | XML |
| Architecture | Native Android + WebView |
| Authentication | Firebase |
| Platform | Android |

## 📂 Project Structure

```text
DailyCart-Android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/getbaskit/customer/
│   │       │           └── MainActivity.kt
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   ├── layout/
│   │       │   ├── mipmap-*/
│   │       │   └── values/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── screenshots/
│   └── baskit-android-preview.png
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK
- Compatible JDK
- Git

### Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

### Setup

1. Open the project in Android Studio.
2. Allow Gradle to synchronize.
3. Configure Firebase.
4. Add the appropriate `google-services.json`.
5. Enable Firebase Authentication.
6. Configure Google Sign-In.
7. Run the application on an Android device or emulator.

## 🔐 Authentication

DailyCart supports:

- Google Sign-In.
- Email and password registration.
- Email and password login.
- Firebase session management.
- Firebase custom claims for role-based access.

## 📍 Permissions

The application uses Android permissions for:

- Internet connectivity.
- Fine location access.
- Coarse location access.

## 📦 Build APK

1. Open the project in Android Studio.
2. Navigate to **Build**.
3. Select **Generate App Bundles or APKs**.
4. Select **Build APK(s)**.
5. Find the generated APK in the build output directory.

For production releases, generate a signed APK or Android App Bundle.

## 🔒 Security

- HTTPS communication.
- Firebase authentication.
- Android runtime permissions.
- Secure session management.
- Role-based authorization.

Never commit Firebase service account credentials, private signing keys, or passwords to a public repository.

## 👨‍💻 Developer

**Gyanam**

- GitHub: [redux-next](https://github.com/redux-next)
- LinkedIn: [Gyanam Gogoi](https://www.linkedin.com/in/gyanam-gogoi-0b9ab720a/)

---

**DailyCart 🛒**

*Freshness at Your Doorstep.*
