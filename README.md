# TipJar

A one-screen tip calculator for Android, built with Kotlin and Jetpack Compose (Material 3).

- Bill amount with a decimal keyboard, tip of 15%, 18% (default) or 20%, and a people stepper (minimum 1).
- Tip, Total and Each person pays update as you type, in US dollars.
- Money is kept in whole cents (`Long`). The tip rounds half up to the nearest cent; each share rounds up to the next cent so the group never pays less than the total. See `app/src/main/java/com/acme/tipjar/TipCalculator.kt`.

## Build and test

```sh
./gradlew testDebugUnitTest
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

See `.agents/skills/android-emulator/SKILL.md` for setting up the SDK and an emulator.
