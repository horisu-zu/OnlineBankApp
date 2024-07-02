package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Light) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SlightlyGrey,
                focusedIndicatorColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedTextColor = Color.DarkGray,
                focusedTextColor = Color.DarkGray
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, fontSize = 10.sp, fontWeight = FontWeight.Light) },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun DateSelectorPreview() {
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear..(currentYear + 10)).map { it.toString().takeLast(2) }

    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DateSelector(
                selectedValue = "01",
                onValueSelected = {},
                label = "Month",
                options = months,
                modifier = Modifier.weight(1f)
            )

            DateSelector(
                selectedValue = currentYear.toString().takeLast(2),
                onValueSelected = {},
                label = "Year",
                options = years,
                modifier = Modifier.weight(1f)
            )
        }
    }
}*/
