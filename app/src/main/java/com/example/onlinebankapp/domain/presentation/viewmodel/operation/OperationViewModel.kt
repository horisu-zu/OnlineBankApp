package com.example.onlinebankapp.domain.presentation.viewmodel.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.repository.OperationRepository
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OperationViewModel(private val operationRepository: OperationRepository): ViewModel() {
    val _operationState = MutableStateFlow<Resource<List<OperationData>>>(Resource.Loading())
    val operationState: StateFlow<Resource<List<OperationData>>> = _operationState.asStateFlow()

    val _typeState = MutableStateFlow<Resource<List<OperationType>>>(Resource.Loading())
    val typeState: StateFlow<Resource<List<OperationType>>> = _typeState.asStateFlow()

    fun getOperations(typeId: String? = null) {
        viewModelScope.launch {
            val flow = if (typeId != null) {
                operationRepository.getOperationsByType(typeId)
            } else {
                operationRepository.getOperations()
            }
            flow.collect { result ->
                _operationState.value = result
            }
        }
    }

    fun getOperationsCountForType(typeId: String): Int {
        return when (val state = _operationState.value) {
            is Resource.Success -> state.data!!.count { it.operationTypeId == typeId }
            else -> 0
        }
    }

    fun getOperationTypes() {
        viewModelScope.launch {
            operationRepository.getOperationTypes().collect() { result ->
                _typeState.value = result
            }
        }
    }
}