package com.example.onlinebankapp.domain.presentation.cardsection

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.util.Resource

@Composable
fun OperationComponent(
    quickOperationsState: Resource<List<String>>,
    operationsState: Resource<List<OperationData>>,
    selectedOperationsState: Resource<List<OperationData>>,
    onSelectedOperationsChange: (List<OperationData>) -> Unit,
    onSaveClick: () -> Unit
) {
    when {
        quickOperationsState is Resource.Loading || operationsState is Resource.Loading ||
                selectedOperationsState is Resource.Loading -> {}
        quickOperationsState is Resource.Error -> {
            ErrorMessage("Error loading quick operations: ${quickOperationsState.message}")
        }
        operationsState is Resource.Error -> {
            ErrorMessage("Error loading operations: ${operationsState.message}")
        }
        selectedOperationsState is Resource.Error -> {
            ErrorMessage("Error loading selected operations: ${selectedOperationsState.message}")
        }
        quickOperationsState is Resource.Success &&
                operationsState is Resource.Success &&
                selectedOperationsState is Resource.Success -> {
            val selectedOperations = selectedOperationsState.data ?: emptyList()
            Log.d("Selected Operations", "Size: ${selectedOperations.size}")
            OperationList(
                operations = operationsState.data ?: emptyList(),
                selectedOperations = selectedOperations,
                onSelectedOperationsChange = onSelectedOperationsChange,
                onSaveClick = onSaveClick
            )
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(16.dp)
    )
}
