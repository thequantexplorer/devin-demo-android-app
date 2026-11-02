---
name: android-emulator
description: Install the Android SDK on a fresh Linux machine, build TipJar, start an Android emulator, install the app, and take screenshots or a screen recording. Use for any task that builds, runs or tests this app.
---

# Build and run TipJar on an Android emulator

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

`settings.gradle.kts` lists Google's mirror of Maven Central first. Keep it: Maven Central often answers HTTP 429 to shared cloud machines.

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
