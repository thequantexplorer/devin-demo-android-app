---
name: design
description: Visual design for the TipJar tip calculator screen. Read before building or changing the UI.
---

## Direction: clean money app

A calm, confident Material 3 screen with a custom green theme. Do not use the default Material purple theme.

- Add a custom `TipJarTheme` with light and dark color schemes. Light: primary `#1E7F55`, onPrimary white, primaryContainer `#CDEFD9`, background and surface `#F6F7F3`, surfaceVariant `#E8EBE4`, onSurface `#14181A`. Dark: primary `#6FD8A0`, background and surface `#0F1412`, surfaceVariant `#1E2622`, onSurface `#E6EAE6`. Use it in `MainActivity` and in previews. Draw edge to edge with correct status bar padding.
- Top of the screen: "TipJar" as a large title and a one-line subtitle "Split the bill" in the secondary color.
- Bill amount: a large outlined text field with a `$` prefix, 28 sp text, decimal keyboard.
- Tip: a `SingleChoiceSegmentedButtonRow` with 15%, 18% and 20%.
- People: a row with the label on the left and a stepper on the right (round outlined − and + icon buttons with the count between them in 22 sp, tabular numbers). Disable − at 1.
- Results: one card in `primaryContainer`, rounded 28 dp. "Each person pays" as a small label and the amount very large (56 sp, medium weight). Under it, Tip and Total as two rows with the label on the left and the amount on the right, split by a thin divider.
- Animate the big amount when it changes (`AnimatedContent` with a quick fade or slide).
- 24 dp side padding, 16 dp between groups. Check light and dark.

States to screenshot: the empty screen (all `$0.00`), the filled result (42.50, 18%, 2 people), and the same in dark theme.

## Rules for every screen

- One clear visual direction, used everywhere. No default framework or browser look (no unstyled inputs, no default blue links, no stock Material purple).
- Sans-serif type only. Use a real type scale with a few sizes, not many. Large, confident headings; quiet secondary text.
- Mostly neutral colors plus one accent. Use color for meaning (selected, done, error), not decoration. No purple or pink.
- Even spacing on an 8 px grid. Generous margins. Line up edges.
- Every interactive element has hover (or pressed), focus and disabled states.
- Design the empty state, the error state and the full state, not just one.
- Small, quick motion (150 to 250 ms) when things appear, change or finish.
- Support light and dark appearance if the platform has it, and check both.

## Check before you open the pull request

1. Take screenshots of every screen and state in the list above.
2. Look at each one at full size. Fix anything that is misaligned, cramped, clipped, low contrast or looks unfinished.
3. Put the final screenshots in the pull request description.
