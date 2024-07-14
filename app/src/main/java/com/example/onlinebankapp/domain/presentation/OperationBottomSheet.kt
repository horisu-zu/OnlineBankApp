package com.example.onlinebankapp.domain.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.presentation.history.getSoftColor
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationBottomSheet(
    operationsList: List<OperationData>,
    initialSelectedOperations: List<OperationData>,
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onSelectedOperationsChange: (List<OperationData>) -> Unit,
    onSaveClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedOperations by remember(showBottomSheet) {
        mutableStateOf(initialSelectedOperations)
    }

    Log.d("Selected Bottom Sheet Operations", selectedOperations.size.toString())

    if(showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissRequest()
            },
            sheetState = sheetState,
            containerColor = SlightlyGrey
        ) {
            OperationsList(
                operationsList = operationsList,
                selectedOperations = selectedOperations,
                onOperationToggle = { operation, checked ->
                    selectedOperations = if (checked) {
                        selectedOperations + operation
                    } else {
                        selectedOperations - operation
                    }
                }
            )
            OperationButton(
                label = "Save Changes",
                onClick = {
                    onSelectedOperationsChange(selectedOperations)
                    onSaveClick()
                },
                enabled = listsComparison(initialSelectedOperations, selectedOperations),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 24.dp)
            )
        }

        LaunchedEffect(sheetState) {
            scope.launch {
                sheetState.show()
            }
        }
    }
}

private fun listsComparison(list1: List<OperationData>, list2: List<OperationData>): Boolean {
    if (list1.size != list2.size) return true
    return list1.any { it !in list2 } || list2.any { it !in list1 }
}

@Composable
fun OperationsList(
    operationsList: List<OperationData>,
    selectedOperations: List<OperationData>,
    onOperationToggle: (OperationData, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(operationsList) { operation ->
            val isChecked = selectedOperations.contains(operation)
            val position = selectedOperations.indexOf(operation)

            OperationItem(
                operationData = operation,
                isChecked = isChecked,
                position = if (position != -1) position else null,
                maxCount = selectedOperations.size,
                onCheckedChange = { checked ->
                    onOperationToggle(operation, checked)
                }
            )
        }
    }
}

@Composable
private fun OperationItem(
    operationData: OperationData,
    isChecked: Boolean,
    position: Int?,
    maxCount: Int,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(getSoftColor(operationData.iconColor, 0.25f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = operationData.icon),
                contentDescription = operationData.title,
                tint = operationData.iconColor,
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = operationData.title,
            color = Color.DarkGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (isChecked && position != null) {
            Text(
                text = "${position + 1}/$maxCount",
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF33691E),
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
    }
}
