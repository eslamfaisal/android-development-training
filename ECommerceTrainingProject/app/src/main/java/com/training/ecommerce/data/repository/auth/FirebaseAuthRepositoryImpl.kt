package com.training.ecommerce.data.repository.auth

import com.training.ecommerce.data.datasource.firebase.FirebaseAuthDataSource
import com.training.ecommerce.data.models.Resource
import kotlinx.coroutines.flow.Flow

class FirebaseAuthRepositoryImpl(private val authDataSource: FirebaseAuthDataSource) :
    FirebaseAuthRepository {

    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<String>> {
        return authDataSource.loginWithEmailAndPassword(email, password)
    }

}