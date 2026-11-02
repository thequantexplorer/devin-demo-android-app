# Spec: tip calculator screen

TipJar's first real feature. Today the app only shows "Tip calculator coming soon."

## Tip math (`TipCalculator.calculate`)

- Money is in whole cents (`Long`). Never use `Float` or `Double` for money.
- `tipCents` = bill × tip percent ÷ 100, rounded half up to the nearest cent.
- `totalCents` = bill + tip.
- `perPersonCents` = total ÷ people, rounded **up** to the next cent, so the group never pays less than the total.
- Throw `IllegalArgumentException` if the bill is negative, the tip percent is negative, or people is less than 1.

The tests in `app/src/test/java/com/acme/tipjar/TipCalculatorTest.kt` cover these rules. They fail today.

## Screen

Replace the "coming soon" text with the calculator. One screen, no navigation.

1. **Bill amount** text field with a decimal keyboard, for example `42.50`.
2. **Tip** choice of three buttons: 15%, 18%, 20%. 18% is selected at first.
3. **People** stepper with − and + buttons. It starts at 1 and can't go below 1.
4. Results that update as you type:
   - Tip
   - Total
   - Each person pays
5. Show amounts as US dollars with two decimals, for example `$7.65`.
6. If the bill field is empty or not a number, show `$0.00` for every result. Don't crash.

## Acceptance check

Bill `42.50`, tip 18%, 2 people → Tip `$7.65`, Total `$50.15`, Each person pays `$25.08`.
