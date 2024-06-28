package com.example.onlinebankapp.domain.user

import java.util.Date

data class UserData(
    val userId: String = "",
    val email: String = "",
    val userName: String = "",
    val phoneNumber: String? = null,
    val createdAt: Date = Date(),
    val signedIn: Date = Date()
)