package com.example.onlinebankapp.domain.presentation.viewmodel.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onlinebankapp.domain.repository.OperationRepository

class OperationViewModelFactory(private val repository: OperationRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OperationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}