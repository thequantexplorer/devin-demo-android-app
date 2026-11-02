package com.acme.tipjar.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acme.tipjar.Money
import com.acme.tipjar.TipCalculator
import com.acme.tipjar.TipResult
import com.acme.tipjar.ui.theme.TipJarTheme

private val TipOptions = listOf(15, 18, 20)
private const val DEFAULT_TIP_PERCENT = 18
private const val MAX_PEOPLE = 99
private const val TABULAR_NUMBERS = "tnum"

@Composable
fun HomeScreen(
    initialBill: String = "",
    initialTipPercent: Int = DEFAULT_TIP_PERCENT,
    initialPeople: Int = 1,
) {
    var billText by rememberSaveable { mutableStateOf(initialBill) }
    var tipPercent by rememberSaveable { mutableIntStateOf(initialTipPercent) }
    var people by rememberSaveable { mutableIntStateOf(initialPeople) }

    val billCents = Money.parseBillCents(billText)
    val result = TipCalculator.calculate(billCents ?: 0, tipPercent, people)
    val billError = billText.isNotBlank() && billCents == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Header()
        Spacer(Modifier.height(8.dp))
        BillField(
            text = billText,
            isError = billError,
            onTextChange = { if (Money.isAcceptableBillInput(it)) billText = it },
        )
        TipChoice(selected = tipPercent, onSelect = { tipPercent = it })
        PeopleStepper(
            count = people,
            onDecrement = { people = (people - 1).coerceAtLeast(1) },
            onIncrement = { people = (people + 1).coerceAtMost(MAX_PEOPLE) },
        )
        Spacer(Modifier.height(8.dp))
        ResultCard(result)
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            "TipJar",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            "Split the bill",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun BillField(text: String, isError: Boolean, onTextChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Bill amount") },
        placeholder = {
            Text("0.00", style = AmountFieldStyle, color = MaterialTheme.colorScheme.outline)
        },
        prefix = { Text("$", style = AmountFieldStyle, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        textStyle = AmountFieldStyle,
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            { Text("Enter an amount like 42.50") }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
    )
}

private val AmountFieldStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Medium, fontFeatureSettings = TABULAR_NUMBERS)

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipChoice(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionLabel("Tip")
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            TipOptions.forEachIndexed { index, percent ->
                SegmentedButton(
                    selected = percent == selected,
                    onClick = { onSelect(percent) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = TipOptions.size),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    ),
                    icon = {},
                    modifier = Modifier.height(48.dp),
                ) {
                    Text("$percent%", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun PeopleStepper(count: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SectionLabel("People")
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedIconButton(
                onClick = onDecrement,
                enabled = count > 1,
                border = stepperBorder(enabled = count > 1),
                modifier = Modifier.size(48.dp),
            ) {
                Icon(MinusIcon, contentDescription = "Fewer people")
            }
            Text(
                count.toString(),
                modifier = Modifier.widthIn(min = 56.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium, fontFeatureSettings = TABULAR_NUMBERS),
                color = MaterialTheme.colorScheme.onSurface,
            )
            OutlinedIconButton(
                onClick = onIncrement,
                enabled = count < MAX_PEOPLE,
                border = stepperBorder(enabled = count < MAX_PEOPLE),
                modifier = Modifier.size(48.dp),
            ) {
                Icon(PlusIcon, contentDescription = "More people")
            }
        }
    }
}

@Composable
private fun stepperBorder(enabled: Boolean) = BorderStroke(
    width = 1.dp,
    color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant,
)

@Composable
private fun ResultCard(result: TipResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Each person pays", style = MaterialTheme.typography.labelLarge)
            AnimatedContent(
                targetState = Money.formatDollars(result.perPersonCents),
                transitionSpec = {
                    (fadeIn(tween(200)) + slideInVertically(tween(200)) { it / 4 }) togetherWith
                        (fadeOut(tween(150)) + slideOutVertically(tween(150)) { -it / 4 })
                },
                label = "perPerson",
            ) { amount ->
                Text(
                    amount,
                    style = TextStyle(
                        fontSize = 56.sp,
                        lineHeight = 64.sp,
                        fontWeight = FontWeight.Medium,
                        fontFeatureSettings = TABULAR_NUMBERS,
                    ),
                    maxLines = 1,
                )
            }
            Spacer(Modifier.height(16.dp))
            ResultRow("Tip", Money.formatDollars(result.tipCents))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            )
            ResultRow("Total", Money.formatDollars(result.totalCents))
        }
    }
}

@Composable
private fun ResultRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            amount,
            style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = TABULAR_NUMBERS),
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private val MinusIcon: ImageVector = strokeIcon("Minus") {
    moveTo(6f, 12f)
    lineTo(18f, 12f)
}

private val PlusIcon: ImageVector = strokeIcon("Plus") {
    moveTo(6f, 12f)
    lineTo(18f, 12f)
    moveTo(12f, 6f)
    lineTo(12f, 18f)
}

private fun strokeIcon(name: String, pathBuilder: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit) =
    ImageVector.Builder(name = name, defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            pathBuilder = pathBuilder,
        )
        .build()

@Preview(name = "Empty", showBackground = true)
@Composable
private fun HomeScreenEmptyPreview() {
    TipJarTheme(darkTheme = false) { Surface { HomeScreen() } }
}

@Preview(name = "Filled", showBackground = true)
@Composable
private fun HomeScreenFilledPreview() {
    TipJarTheme(darkTheme = false) { Surface { HomeScreen(initialBill = "42.50", initialPeople = 2) } }
}

@Preview(name = "Filled dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    TipJarTheme(darkTheme = true) { Surface { HomeScreen(initialBill = "42.50", initialPeople = 2) } }
}
