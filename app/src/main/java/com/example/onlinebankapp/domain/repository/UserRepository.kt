package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {

    suspend fun getUser(userId: String): Flow<Resource<UserData>>
    suspend fun updateUser(userId: String, user: UserData): Flow<Resource<Void?>>
    suspend fun updateSignedInStatus(userId: String, signedIn: Date)
    suspend fun addUser(userId: String, user: UserData): Flow<Resource<Void?>>
}