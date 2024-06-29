package com.example.onlinebankapp.domain.presentation.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.repository.UserRepository
import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<Resource<UserData>>(Resource.Loading())
    val authState: StateFlow<Resource<UserData>> = _authState.asStateFlow()

    private val _userState = MutableStateFlow<Resource<UserData>>(Resource.Loading())
    val userState: StateFlow<Resource<UserData>> = _userState.asStateFlow()

    fun authenticateUser(email: String, password: String, username: String = "Username",
                         isRegistration: Boolean = false) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            try {
                val result = if (isRegistration) {
                    signUpWith(email, password)
                } else {
                    signInWith(email, password)
                }
                when (result) {
                    is Resource.Success -> {
                        val user = result.data
                        if (user != null) {
                            if (isRegistration) {
                                addUser(user.uid, email, username)
                            } else {
                                getUser(user.uid)
                                updateUserSignedInStatus(user.uid)
                            }
                        } else {
                            _authState.value = Resource
                                .Error("Authentication successful but user is null")
                        }
                    }
                    is Resource.Error -> {
                        _authState.value = Resource.Error(result.message
                            ?: "Authentication failed")
                    }
                    is Resource.Loading -> {
                        _authState.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "Authentication failed")
            }
        }
    }

    private suspend fun signInWith(email: String, password: String): Resource<FirebaseUser> =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Resource.Success(task.result?.user))
                    } else {
                        continuation.resume(Resource.Error(task.exception?.message
                            ?: "Sign in failed"))
                    }
                }
        }

    private suspend fun signUpWith(email: String, password: String): Resource<FirebaseUser> =
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Resource.Success(task.result?.user))
                    } else {
                        continuation.resume(Resource.Error(task.exception?.message
                            ?: "Sign up failed"))
                    }
                }
        }

    private fun addUser(userId: String, email: String, username: String) {
        viewModelScope.launch {
            val userData = UserData(
                userId = userId,
                email = email,
                userName = username,
                createdAt = Date(),
                signedIn = Date()
            )
            repository.addUser(userId, userData).collect { result ->
                _authState.value = when (result) {
                    is Resource.Success -> Resource.Success(userData)
                    is Resource.Error -> Resource.Error(result.message
                        ?: "Failed to add user to database")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.d("UserViewModel", "Current user found: ${currentUser.uid}")
                getUser(currentUser.uid)
            } else {
                Log.d("UserViewModel", "No user signed in")
                _userState.value = Resource.Error("No user is signed in")
            }
        }
    }

    private fun getUser(userId: String) {
        viewModelScope.launch {
            repository.getUser(userId).collect { result ->
                _userState.value = result
                if (result is Resource.Success) {
                    _authState.value = result
                }
            }
        }
    }

    private fun updateUserSignedInStatus(userId: String) {
        viewModelScope.launch {
            try {
                val lastSignInDate = Date()
                repository.updateSignedInStatus(userId, lastSignInDate)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to update signed in status", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _authState.value = Resource.Success(null)
                _userState.value = Resource.Loading()
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "Logout failed")
            }
        }
    }
}