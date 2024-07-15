package com.example.onlinebankapp.data.repository

import android.util.Log
import com.example.onlinebankapp.domain.repository.OperationRepository
import com.example.onlinebankapp.domain.repository.UserRepository
import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val operationRepository: OperationRepository
): UserRepository {
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
            val defaultOperations = operationRepository.getDefaultOperations()
            val updatedUser = user.copy(quickOperations = defaultOperations)

            val result = firestore.collection("users")
                .document(userId)
                .set(updatedUser)
                .await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getQuickOperations(userId: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            val documentSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val quickOperations = documentSnapshot.get("quickOperations") as? List<String>
            if (quickOperations != null) {
                emit(Resource.Success(quickOperations))
            } else {
                emit(Resource.Error("Quick operations not found or invalid"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateQuickOperations(
        userId: String,
        quickOperations: List<String>
    ): Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())

        if (quickOperations.isEmpty()) {
            emit(Resource.Error("Quick operations list is empty"))
            return@flow
        }

        val documentRef = firestore.collection("users").document(userId)

        try {
            val documentSnapshot = documentRef.get().await()
            if (documentSnapshot.exists()) {
                documentRef.update("quickOperations", quickOperations).await()
                emit(Resource.Success(null))
            } else {
                emit(Resource.Error("Document does not exist for userId: $userId"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}