package com.example.onlinebankapp.domain.presentation.viewmodel.operation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.repository.OperationRepository
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OperationViewModel(private val operationRepository: OperationRepository): ViewModel() {
    val _operationState = MutableStateFlow<Resource<List<OperationData>>>(Resource.Loading())
    val operationState: StateFlow<Resource<List<OperationData>>> = _operationState.asStateFlow()

    val _typeState = MutableStateFlow<Resource<List<OperationType>>>(Resource.Loading())
    val typeState: StateFlow<Resource<List<OperationType>>> = _typeState.asStateFlow()

    private val _selectedOperations = MutableStateFlow<Resource<List<OperationData>>>(Resource.Loading())
    val selectedOperations: StateFlow<Resource<List<OperationData>>> = _selectedOperations

    init {
        getOperations()
        getOperationTypes()
    }

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

    fun getOperationsCountForType(typeId: String): StateFlow<Int> {
        return operationState.map { state ->
            when (state) {
                is Resource.Success -> state.data!!.count { it.operationTypeId == typeId }
                else -> 0
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, 0)
    }

    fun getSingleOperationIdForType(typeId: String): StateFlow<String?> {
        return operationState.map { state ->
            when (state) {
                is Resource.Success -> {
                    val operations = state.data!!.filter { it.operationTypeId == typeId }
                    if (operations.size == 1) operations.first().operationId else null
                }
                else -> null
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun getOperationTypes() {
        viewModelScope.launch {
            operationRepository.getOperationTypes().collect() { result ->
                _typeState.value = result
            }
        }
    }

    fun getOperationsByIds(ids: List<String>) {
        viewModelScope.launch {
            _selectedOperations.value = Resource.Loading()
            operationState.collect { state ->
                _selectedOperations.value = when (state) {
                    is Resource.Success -> {
                        val operationsMap = state.data?.associateBy { it.operationId } ?: emptyMap()
                        val filteredOperations = ids.mapNotNull { id -> operationsMap[id] }
                        Resource.Success(filteredOperations)
                    }
                    is Resource.Error -> Resource.Error(state.message ?: "Unknown error")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }
}