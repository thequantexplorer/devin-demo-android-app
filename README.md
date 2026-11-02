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

The home screen says "Tip calculator coming soon." The tip math in `TipCalculator.kt` is not written yet, so the unit tests fail:

```text
$ ./gradlew testDebugUnitTest
TipCalculatorTest > tip rounds half up to the nearest cent FAILED
    kotlin.NotImplementedError at TipCalculatorTest.kt:17
TipCalculatorTest > zero people is rejected FAILED
    java.lang.Exception at ExpectException.java:30
        Caused by: kotlin.NotImplementedError at TipCalculatorTest.kt:30
TipCalculatorTest > 15 percent of 50 dollars for one person FAILED
    kotlin.NotImplementedError at TipCalculatorTest.kt:11
TipCalculatorTest > split rounds each share up so the group never pays less than the total FAILED
    kotlin.NotImplementedError at TipCalculatorTest.kt:23
TipCalculatorTest > negative bill is rejected FAILED
    java.lang.Exception at ExpectException.java:30
        Caused by: kotlin.NotImplementedError at TipCalculatorTest.kt:35
5 tests completed, 5 failed
BUILD FAILED
```

What to build is in [`docs/specs/tip-calculator.md`](docs/specs/tip-calculator.md).

---

This repo is a starter for a Devin demo. See [DEVIN_DEMO.md](DEVIN_DEMO.md) for setup and the prompt.
