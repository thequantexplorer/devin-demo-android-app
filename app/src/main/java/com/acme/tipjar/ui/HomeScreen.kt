package com.acme.tipjar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acme.tipjar.Money
import com.acme.tipjar.TipCalculator
import com.acme.tipjar.TipResult

private val TIP_OPTIONS = listOf(15, 18, 20)

@Composable
fun HomeScreen() {
    var billText by rememberSaveable { mutableStateOf("") }
    var tipPercent by rememberSaveable { mutableIntStateOf(18) }
    var people by rememberSaveable { mutableIntStateOf(1) }

    val result = Money.parseDollars(billText)?.let { billCents ->
        runCatching { TipCalculator.calculate(billCents, tipPercent, people) }.getOrNull()
    } ?: TipResult(tipCents = 0, totalCents = 0, perPersonCents = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text("TipJar", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = billText,
            onValueChange = { billText = it },
            label = { Text("Bill amount") },
            placeholder = { Text("42.50") },
            prefix = { Text("$") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
        )

        TipSelector(selected = tipPercent, onSelect = { tipPercent = it })

        PeopleStepper(people = people, onChange = { people = it })

        Results(result)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipSelector(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Tip", style = MaterialTheme.typography.titleMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            TIP_OPTIONS.forEachIndexed { index, percent ->
                SegmentedButton(
                    selected = percent == selected,
                    onClick = { onSelect(percent) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = TIP_OPTIONS.size),
                ) {
                    Text("$percent%")
                }
            }
        }
    }
}

@Composable
private fun PeopleStepper(people: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("People", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = { onChange(people - 1) },
            enabled = people > 1,
            modifier = Modifier.semantics { contentDescription = "Fewer people" },
        ) {
            Text("−")
        }
        Text(
            people.toString(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        OutlinedButton(
            onClick = { onChange(people + 1) },
            modifier = Modifier.semantics { contentDescription = "More people" },
        ) {
            Text("+")
        }
    }
}

@Composable
private fun Results(result: TipResult) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ResultRow("Tip", result.tipCents)
            ResultRow("Total", result.totalCents)
            HorizontalDivider()
            ResultRow("Each person pays", result.perPersonCents, emphasized = true)
        }
    }
}

@Composable
private fun ResultRow(label: String, cents: Long, emphasized: Boolean = false) {
    val style = if (emphasized) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = style, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(16.dp))
        Text(
            Money.formatDollars(cents),
            style = style,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme { HomeScreen() }
}
