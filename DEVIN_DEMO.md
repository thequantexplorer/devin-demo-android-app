# Devin demo: build an Android app and run it on the emulator

**What it shows:** Devin adds a tip calculator screen to a small Kotlin + Jetpack Compose app, makes the unit tests pass, starts an Android emulator on its own machine, installs the app, and sends you screenshots and a recording of it working.

**Needs:** GitHub

More demos: [devin-advanced-demos](https://github.com/thequantexplorer/devin-advanced-demos)

## Set it up

1. Click **Use this template → Create a new repository**, choose your GitHub account or org as the owner, and keep the name `devin-demo-android-app`. The prompt below assumes that name. Use the template rather than a fork: pull requests on a fork go to the original repo by default.
2. In Devin, open [**Settings → Connections → GitHub**](https://app.devin.ai/settings/connections/github) and make sure Devin can access the new repository.

You don't need Android Studio or anything on your computer. Devin installs the Android SDK and runs the emulator on its own machine. The steps it follows are in [`.agents/skills/android-emulator/SKILL.md`](.agents/skills/android-emulator/SKILL.md).

## The starting problem

The app only says "Tip calculator coming soon", and the tip math isn't written, so the unit tests fail:

```text
$ ./gradlew testDebugUnitTest
5 tests completed, 5 failed
BUILD FAILED
```

## Prompt

Start a new Devin session at [app.devin.ai](https://app.devin.ai) and paste this. Replace `<you>` with your GitHub user or org.

```text
Repo: https://github.com/<you>/devin-demo-android-app

Build the tip calculator screen described in docs/specs/tip-calculator.md. Follow the repo skill in .agents/skills/android-emulator/SKILL.md to install the Android SDK, build the app and run the emulator on your machine.

1. Run ./gradlew testDebugUnitTest first and show me the failing output.
2. Implement TipCalculator so the existing tests pass. Don't change the tests. Add tests for the dollar formatting.
3. Replace the "coming soon" text with the calculator screen from the spec.
4. Run ./gradlew testDebugUnitTest again and show me the passing output.
5. Start an Android emulator on your machine, install the debug APK and open the app. Enter a bill of 42.50, pick 18%, set people to 2, and check it shows Tip $7.65, Total $50.15, Each person pays $25.08.
6. Take a screenshot of the empty screen and of the filled-in result, and record your screen while you go through step 5.

Open a PR with the before and after test output and the screenshots. Send me the screenshots and the recording here. Tell me exactly what worked on the emulator and anything that didn't.
```

## What to watch

- Open the **Desktop** tab in the session to see the emulator window while Devin uses the app.
- It runs the unit tests and shows you the real output, before and after.

## Done when

- There is a PR with the tip calculator screen and a passing `./gradlew testDebugUnitTest` run.
- The PR or session has screenshots and a recording of the app on the emulator showing Tip $7.65, Total $50.15 and Each person pays $25.08.

## Next

Ask Devin to set up Android in your [environment blueprint](https://docs.devin.ai/onboard-devin/environment/android-emulation) so later sessions start with the SDK and emulator already installed.
