package com.training.ecommerce.data.repository.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FirebaseAuthRepository {

    // Example usage for email and password login
    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<UserDetailsModel>> {
        return login { auth.signInWithEmailAndPassword(email, password).await() }
    }

    // Example usage for Google login
    override suspend fun loginWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> {
        return login {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
        }
    }

    // Example usage for Facebook login
    override suspend fun loginWithFacebook(token: String): Flow<Resource<UserDetailsModel>> {
        return login {
            val credential = FacebookAuthProvider.getCredential(token)
            auth.signInWithCredential(credential).await()
        }
    }

    private fun login(
        signInRequest: suspend () -> AuthResult
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())
            val authResult = signInRequest() // Invoke the passed lambda action to perform login
            val userId = authResult.user?.uid!!
            val userDoc = firestore.collection("users").document(userId).get().await()
            userDoc?.let {
                val userDetails = it.toObject(UserDetailsModel::class.java)
                emit(Resource.Success(userDetails!!))
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e)) // Emit error
        }
    }

    override fun logout() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }
}