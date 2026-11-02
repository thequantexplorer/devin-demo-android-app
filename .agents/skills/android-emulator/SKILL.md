---
name: android-emulator
description: Create the TipJar Android project, install the Android SDK on a fresh Linux machine, build the app, start an Android emulator, install the app, and take screenshots or a screen recording. Use for any task that builds, runs or tests this app.
---

# Build and run TipJar on an Android emulator

## 0. Create the project (only if there is no `settings.gradle.kts` yet)

Use these versions. They are known to build together on a Devin machine.

- Kotlin + Jetpack Compose + Material 3, one module `:app`, package and application ID `com.acme.tipjar`.
- Gradle 8.9 with the Gradle wrapper committed (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`). To make the wrapper, download https://services.gradle.org/distributions/gradle-8.9-bin.zip, unzip it, and run `<unzipped>/bin/gradle wrapper --gradle-version 8.9` in the repo root.
- Root `build.gradle.kts` plugins: `com.android.application` 8.5.2, `org.jetbrains.kotlin.android` 2.0.20 and `org.jetbrains.kotlin.plugin.compose` 2.0.20, all `apply false`.
- `app/build.gradle.kts`: `compileSdk = 34`, `targetSdk = 34`, `minSdk = 26`, Java and `jvmTarget` 17, `buildFeatures { compose = true }`. Dependencies: `platform("androidx.compose:compose-bom:2024.09.00")`, `androidx.core:core-ktx:1.13.1`, `androidx.activity:activity-compose:1.9.2`, `androidx.compose.ui:ui`, `androidx.compose.material3:material3`, `androidx.compose.ui:ui-tooling-preview`, `debugImplementation("androidx.compose.ui:ui-tooling")`, and `testImplementation("junit:junit:4.13.2")` for plain JVM unit tests under `app/src/test`.
- `settings.gradle.kts`: in both `pluginManagement` and `dependencyResolutionManagement`, list `google()`, then Google's mirror of Maven Central `maven("https://maven-central.storage-download.googleapis.com/maven2/")`, then `mavenCentral()` (and `gradlePluginPortal()` for plugins). Maven Central often answers HTTP 429 to shared cloud machines, and the mirror does not.
- Add a `.gitignore` for `.gradle/`, `build/`, `local.properties` and `.idea/`.

These steps work on a fresh Linux x86_64 machine with JDK 17 and `/dev/kvm`. They were checked on a Devin machine (2 CPUs, 8 GB RAM).

## 1. Install the Android SDK (about 1–3 minutes)

Skip this if `$HOME/android-sdk/platform-tools/adb` already exists.

```sh
export ANDROID_HOME="$HOME/android-sdk"
mkdir -p "$ANDROID_HOME/cmdline-tools" && cd "$ANDROID_HOME/cmdline-tools"
curl -fsSL https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -o tools.zip
unzip -q tools.zip -d tmp && mv tmp/cmdline-tools latest && rm -rf tools.zip tmp
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
yes | sdkmanager --licenses > /dev/null
sdkmanager "platform-tools" "build-tools;34.0.0" "platforms;android-34" "emulator" "system-images;android-34;google_apis;x86_64"
```

Put the two `export` lines in every new shell (or in `~/.bashrc`).

## 2. Build and test (first build about 2–4 minutes)

From the repo root:

```sh
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

If a download fails with HTTP 429, check that `settings.gradle.kts` lists Google's mirror of Maven Central before `mavenCentral()`.

## 3. Start the emulator (boot about 1–2 minutes)

The user is not in the `kvm` group on a fresh machine, so open up `/dev/kvm` first.

```sh
sudo chmod 666 /dev/kvm
echo no | avdmanager create avd -n devin -k "system-images;android-34;google_apis;x86_64" --device pixel_6
DISPLAY=:0 nohup emulator -avd devin -no-snapshot -no-audio -no-boot-anim \
  -gpu swiftshader_indirect -memory 2048 -cores 2 > /tmp/emulator.log 2>&1 &
adb wait-for-device
until [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; do sleep 3; done
```

`DISPLAY=:0` puts the emulator window on the desktop, so the user can watch it and the screen recording tool captures it.

Right after boot the machine is busy, and Android may show **"System UI isn't responding"**. Tap **Wait** (or run `adb shell input keyevent KEYCODE_BACK`) and give it 30–60 seconds. It goes away by itself.

The emulator window may also show a **Compatibility Warnings** box about CPU cores. Click **OK**. It does not affect the app.

## 4. Install, open and capture

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.acme.tipjar/.MainActivity
adb exec-out screencap -p > /tmp/tipjar.png
```

- Typing: tap the field, then `adb shell input text 42.50`.
- Screen size is 1080×2400. Find tap positions from a screenshot, or with `adb shell uiautomator dump /sdcard/ui.xml && adb shell cat /sdcard/ui.xml`.
- Phone-only video: `adb shell screenrecord --time-limit 60 /sdcard/demo.mp4`, then `adb pull /sdcard/demo.mp4`.
- Crashes: `adb logcat -d | grep -A 20 "FATAL EXCEPTION"`.
