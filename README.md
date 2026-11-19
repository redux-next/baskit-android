# Baskit Android

\<p align="center">   \<strong>Your everyday essentials, delivered.\</strong> \</p>

\<p align="center">   A native Android WebView application for the Baskit online shopping platform. \</p>

---

## About

Baskit Android brings the Baskit shopping experience to Android devices through a WebView-based application, combining the existing web platform with native Android capabilities.

**Live Website:** [https://getbaskit.netlify.app/](https://getbaskit.netlify.app/)

**GitHub Repository:** [https://github.com/redux-next/baskit-android](https://github.com/redux-next/baskit-android)

## Features

- **WebView Integration** – Access the Baskit shopping platform directly from the Android app.
- **Google Sign-In** – Native Google authentication using Android Credential Manager and Firebase Authentication.
- **Persistent Authentication** – Stay signed in between app launches.
- **Loading Screen** – Custom loading indicator while the website loads.
- **File Upload Support** – Select and upload files through the Android app.
- **Location Access** – Supports location permissions and browser geolocation.
- **Navigation Support** – Android back-button handling within the WebView.
- **Internet Connectivity** – Securely connects to the live Baskit website.

## Tech Stack

| Technology                | Purpose                     |
| ------------------------- | --------------------------- |
| Kotlin                    | Android development         |
| Android Studio            | Development environment     |
| Android WebView           | Web application integration |
| Firebase Authentication   | User authentication         |
| Google Credential Manager | Native Google Sign-In       |
| Gradle                    | Build automation            |
| XML                       | Android UI layouts          |

## Application Details

| Property       | Value                     |
| -------------- | ------------------------- |
| App Name       | Baskit                    |
| Package Name   | `com.getbaskit.customer`  |
| Platform       | Android                   |
| Language       | Kotlin                    |
| Web Platform   | Next.js                   |
| Authentication | Firebase + Google Sign-In |

## Getting Started

### Prerequisites

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- An Android device or emulator

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/redux-next/baskit-android.git
   ```

2. Open the project in Android Studio.

3. Allow Gradle to sync.

4. Connect an Android device or start an emulator.

5. Click **Run** in Android Studio.

### Build APK

To build a debug APK:

```bash
./gradlew assembleDebug
```

To build a release APK:

```bash
./gradlew assembleRelease
```

APK output directory:

```text
app/build/outputs/apk/
```

## Project Structure

```text
Baskit-Android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── README.md
```

## Security

- Never commit signing keys or keystore passwords.
- Keep local configuration and private credentials out of version control.
- Use HTTPS for network communication.
- Request Android permissions only when required.

## Developer

**GYANAM**

- GitHub: [https://github.com/redux-next](https://github.com/redux-next)
- LinkedIn: [https://www.linkedin.com/in/gyanam-gogoi-0b9ab720a/](https://www.linkedin.com/in/gyanam-gogoi-0b9ab720a/)

---

\<p align="center">   Built with Kotlin and Firebase. \</p>
