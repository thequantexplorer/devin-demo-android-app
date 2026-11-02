# TipJar

A small Android app written in Kotlin with Jetpack Compose. It's the starting point for a tip calculator.

| | |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose (Material 3) |
| Build | Gradle 8.9 wrapper, Android Gradle Plugin 8.5 |
| SDK | compileSdk / targetSdk 34, minSdk 26 |
| JDK | 17 |

## Commands

You need the Android SDK and JDK 17. The skill in [`.agents/skills/android-emulator/SKILL.md`](.agents/skills/android-emulator/SKILL.md) shows how to install the SDK and start an emulator on a Linux machine.

```sh
./gradlew testDebugUnitTest   # unit tests
./gradlew assembleDebug       # builds app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.acme.tipjar/.MainActivity
```

## Current state

The home screen is the tip calculator from [`docs/specs/tip-calculator.md`](docs/specs/tip-calculator.md): bill amount, a 15/18/20% tip choice, a people stepper, and live Tip, Total and Each person pays results. Tip math is in `TipCalculator.kt`, dollar parsing and formatting in `Money.kt`, and the green light/dark theme in `ui/theme/Theme.kt`.


---

This repo is a starter for a Devin demo. See [DEVIN_DEMO.md](DEVIN_DEMO.md) for setup and the prompt.
