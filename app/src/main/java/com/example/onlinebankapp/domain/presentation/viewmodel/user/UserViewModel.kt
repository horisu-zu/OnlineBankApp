package com.example.onlinebankapp.domain.presentation.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.presentation.auth.signUpWith
import com.example.onlinebankapp.domain.repository.UserRepository
import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<UserData>>(Resource.Loading())
    val userState: StateFlow<Resource<UserData>> = _userState

    private val _updateState = MutableStateFlow<Resource<Void?>>(Resource.Success(null))
    val updateState: StateFlow<Resource<Void?>> = _updateState

    private val _registrationState = MutableStateFlow<Resource<UserData>>(Resource.Success(null))
    val registrationState: StateFlow<Resource<UserData>> = _registrationState

    private val _logoutState = MutableStateFlow<Resource<Unit>>(Resource.Success(Unit))
    val logoutState: StateFlow<Resource<Unit>> = _logoutState

    fun getUser(userId: String) {
        viewModelScope.launch {
            repository.getUser(userId).collect { result ->
                _userState.value = result
            }
        }
    }

    fun updateUser(userId: String, user: UserData) {
        viewModelScope.launch {
            repository.updateUser(userId, user).collect { result ->
                _updateState.value = result
            }
        }
    }

    fun updateUserSignedInStatus(user: FirebaseUser, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val lastSignInTimestamp = user.metadata?.lastSignInTimestamp
                    ?: System.currentTimeMillis()
                val lastSignInDate = Date(lastSignInTimestamp)
                repository.updateSignedInStatus(user.uid, lastSignInDate)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun registerUser(email: String, password: String, userName: String = "UserName",
                     phoneNumber: String? = "") {
        viewModelScope.launch {
            _registrationState.value = Resource.Loading()
            signUpWith(email, password) { success, userId, error ->
                if (success && userId != null) {
                    val userData = UserData(
                        userId = userId,
                        email = email,
                        userName = userName,
                        phoneNumber = phoneNumber,
                        createdAt = Date(),
                        signedIn = Date()
                    )
                    addUser(userId, userData)
                } else {
                    _registrationState.value = Resource.Error(error
                        ?: "Registration failed")
                }
            }
        }
    }

    private fun addUser(userId: String, user: UserData) {
        viewModelScope.launch {
            repository.addUser(userId, user).collect { result ->
                _registrationState.value = when (result) {
                    is Resource.Success -> Resource.Success(user)
                    is Resource.Error -> Resource.Error(result.message ?:
                        "Failed to add user to database")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading()
            try {
                FirebaseAuth.getInstance().signOut()
                _logoutState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = Resource.Error(e.message ?: "Logout failed")
            }
        }
    }
}