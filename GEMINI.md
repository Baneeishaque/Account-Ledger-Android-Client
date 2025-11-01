# Project Overview

This is the Account Ledger Android Client, an Android application for managing personal accounts. It is a multi-module project built with Gradle.

## Building and Running

The project can be built and run using Gradle. The following are some of the key Gradle tasks:

*   `./gradlew assembleDebug`: Assembles a debug build of the app.
*   `./gradlew assembleRelease`: Assembles a release build of the app.
*   `./gradlew installDebug`: Installs the debug build of the app on a connected device.
*   `./gradlew installRelease`: Installs the release build of the app on a connected device.
*   `./gradlew test`: Runs unit tests.

To run the app, you can use Android Studio or the following Gradle command:

```bash
./gradlew installDebug
```

## Development Conventions

*   **Coding Style:** The project uses Kotlin for Android development.
*   **Testing:** The project includes unit tests, which can be run with `./gradlew test`.
*   **CI/CD:** The project is set up with various CI/CD pipelines, including Azure DevOps, for building and releasing the app.
