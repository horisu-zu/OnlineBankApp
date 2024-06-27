package com.example.onlinebankapp.data.repository

import android.util.Log
import com.example.onlinebankapp.domain.repository.UserRepository
import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class UserRepositoryImpl(private val firestore: FirebaseFirestore): UserRepository {
    override suspend fun getUser(userId: String): Flow<Resource<UserData>> = flow {
        emit(Resource.Loading())
        try {
            val documentSnapshot = firestore.collection("users")
                .document(userId).get().await()
            val user = documentSnapshot.toObject(UserData::class.java)
            if (user != null) {
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateUser(userId: String, user: UserData): Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())
        try {
            val result = firestore.collection("users").document(userId).set(user,
                SetOptions.merge()).await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateSignedInStatus(userId: String, signedIn: Date) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.collection("users").document(userId)

        try {
            val documentSnapshot = documentRef.get().await()
            if (documentSnapshot.exists()) {
                documentRef.update("signedIn", signedIn).await()
            } else {
                Log.e("Firestore", "Document does not exist for userId: $userId")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Failed to update signed-in status: ${e.message}")
        }
    }

    override suspend fun addUser(userId: String, user: UserData): Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())
        try {
            val result = firestore.collection("users").document(userId).set(user).await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}