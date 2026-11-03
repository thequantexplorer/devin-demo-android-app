package com.acme.tipjar.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acme.tipjar.R
import com.acme.tipjar.TipCalculator
import com.acme.tipjar.ui.theme.TipJarTheme

private const val MAX_BILL_INPUT_LENGTH = 13
private const val MOTION_MS = 200
private val TabularNumbers = "tnum"

@Composable
fun TipJarApp() {
    var bill by rememberSaveable { mutableStateOf("") }
    var tipPercent by rememberSaveable { mutableIntStateOf(TipCalculator.DEFAULT_TIP_PERCENT) }
    var people by rememberSaveable { mutableIntStateOf(TipCalculator.MIN_PEOPLE) }

    TipJarScreen(
        bill = bill,
        onBillChange = { if (it.length <= MAX_BILL_INPUT_LENGTH) bill = it },
        tipPercent = tipPercent,
        onTipPercentChange = { tipPercent = it },
        people = people,
        onPeopleChange = {
            people = it.coerceIn(TipCalculator.MIN_PEOPLE, TipCalculator.MAX_PEOPLE)
        },
    )
}

@Composable
fun TipJarScreen(
    bill: String,
    onBillChange: (String) -> Unit,
    tipPercent: Int,
    onTipPercentChange: (Int) -> Unit,
    people: Int,
    onPeopleChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val result = TipCalculator.calculate(bill, tipPercent, people)
    val billIsInvalid = bill.isNotBlank() && TipCalculator.parseBillCents(bill) == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Header()
            BillField(bill = bill, onBillChange = onBillChange, isError = billIsInvalid)
            TipSelector(selected = tipPercent, onSelect = onTipPercentChange)
            PeopleStepper(people = people, onPeopleChange = onPeopleChange)
            Spacer(Modifier.height(8.dp))
            ResultCard(result = result)
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.subtitle),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun BillField(bill: String, onBillChange: (String) -> Unit, isError: Boolean) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = bill,
        onValueChange = onBillChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.bill_amount)) },
        placeholder = {
            Text("0.00", style = LocalBillTextStyle, color = MaterialTheme.colorScheme.outline)
        },
        prefix = {
            Text(
                text = "$",
                style = LocalBillTextStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 4.dp),
            )
        },
        trailingIcon = if (bill.isNotEmpty()) {
            {
                IconButton(onClick = { onBillChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.clear_bill),
                    )
                }
            }
        } else {
            null
        },
        supportingText = if (isError) {
            { Text(stringResource(R.string.bill_error)) }
        } else {
            null
        },
        isError = isError,
        textStyle = LocalBillTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
    )
}

private val LocalBillTextStyle = TextStyle(
    fontSize = 28.sp,
    lineHeight = 36.sp,
    fontWeight = FontWeight.Medium,
    fontFeatureSettings = TabularNumbers,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipSelector(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionLabel(stringResource(R.string.tip))
        val options = TipCalculator.TIP_PERCENTS
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, percent ->
                SegmentedButton(
                    selected = percent == selected,
                    onClick = { onSelect(percent) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                        inactiveBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Text(
                        text = "$percent%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFeatureSettings = TabularNumbers,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PeopleStepper(people: Int, onPeopleChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SectionLabel(stringResource(R.string.people), modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedIconButton(
                onClick = { onPeopleChange(people - 1) },
                enabled = people > TipCalculator.MIN_PEOPLE,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = stringResource(R.string.remove_person),
                )
            }
            Text(
                text = people.toString(),
                modifier = Modifier
                    .widthIn(min = 40.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Medium,
                    fontFeatureSettings = TabularNumbers,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
            OutlinedIconButton(
                onClick = { onPeopleChange(people + 1) },
                enabled = people < TipCalculator.MAX_PEOPLE,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_person),
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ResultCard(result: TipCalculator.Result) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.each_person_pays),
                style = MaterialTheme.typography.labelLarge,
            )
            AnimatedContent(
                targetState = TipCalculator.formatDollars(result.perPersonCents),
                transitionSpec = {
                    (fadeIn(tween(MOTION_MS)) + slideInVertically(tween(MOTION_MS)) { it / 4 })
                        .togetherWith(fadeOut(tween(MOTION_MS / 2)))
                        .using(SizeTransform(clip = false))
                },
                label = "eachPersonPays",
            ) { amount ->
                Text(
                    text = amount,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = TextStyle(
                        fontSize = 56.sp,
                        lineHeight = 64.sp,
                        fontWeight = FontWeight.Medium,
                        fontFeatureSettings = TabularNumbers,
                    ),
                    maxLines = 1,
                )
            }
            Spacer(Modifier.height(16.dp))
            AmountRow(label = stringResource(R.string.tip), amount = result.tipCents)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            )
            AmountRow(label = stringResource(R.string.total), amount = result.totalCents)
        }
    }
}

@Composable
private fun AmountRow(label: String, amount: Long) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = TipCalculator.formatDollars(amount),
            style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = TabularNumbers),
        )
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun EmptyPreview() {
    TipJarTheme(darkTheme = false) {
        TipJarScreen("", {}, TipCalculator.DEFAULT_TIP_PERCENT, {}, 1, {})
    }
}

@Preview(name = "Filled", showBackground = true)
@Composable
private fun FilledPreview() {
    TipJarTheme(darkTheme = false) {
        TipJarScreen("42.50", {}, 18, {}, 2, {})
    }
}

@Preview(name = "Filled dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilledDarkPreview() {
    TipJarTheme(darkTheme = true) {
        TipJarScreen("42.50", {}, 18, {}, 2, {})
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun ErrorPreview() {
    TipJarTheme(darkTheme = false) {
        TipJarScreen("12.3.4", {}, 15, {}, 3, {})
    }
}
