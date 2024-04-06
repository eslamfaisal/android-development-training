package com.training.ecommerce.data.repository.auth

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.user.AuthProvider
import com.training.ecommerce.data.models.user.UserDetailsModel
import com.training.ecommerce.utils.CrashlyticsUtils
import com.training.ecommerce.utils.LoginException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FirebaseAuthRepository {

    // Example usage for email and password login
    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ) = login(AuthProvider.EMAIL) { auth.signInWithEmailAndPassword(email, password).await() }

    override suspend fun loginWithGoogle(idToken: String) = login(AuthProvider.GOOGLE) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    // Example usage for Facebook login
    override suspend fun loginWithFacebook(token: String) = login(AuthProvider.FACEBOOK) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).await()
    }

    private suspend fun login(
        provider: AuthProvider,
        signInRequest: suspend () -> AuthResult,
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())
            // perform firebase auth sign in request
            val authResult = signInRequest()
            val userId = authResult.user?.uid

            if (userId == null) {
                val msg = "Sign in UserID not found"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            // get user details from firestore
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                val msg = "Logged in user not found in firestore"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            // map user details to UserDetailsModel
            val userDetails = userDoc.toObject(UserDetailsModel::class.java)
            userDetails?.let {
                emit(Resource.Success(userDetails))
            } ?: run {
                val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
            }
        } catch (e: Exception) {
            logAuthIssueToCrashlytics(
                e.message ?: "Unknown error from exception = ${e::class.java}", provider.name
            )
            emit(Resource.Error(e)) // Emit error
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user == null) {
                emit(Resource.Error(Exception("User not created")))
                return@flow
            }
            user.sendEmailVerification().await()
            user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(fullName).build())
                .await()
            emit(Resource.Success("Verification email sent"))
            observeEmailVerification(user.uid)
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun observeEmailVerification(userId: String) {
        val listener = FirebaseAuth.IdTokenListener { auth ->
            auth.currentUser?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        GlobalScope.launch {
                            updateUserDetails(
                                userId,
                                user.email.toString(),
                                user.displayName.toString()
                            )
                        }
                    } else {
                        Log.e(TAG, "Email verification failed")
                    }
                } else {
                    Log.e(TAG, "Email verification failed")
                }
            }
        }
        auth.addIdTokenListener(listener)
    }

    private suspend fun updateUserDetails(userId: String, email: String, fullName: String) {
        val userDetails = mapOf(
            "id" to userId,
            "created_at" to System.currentTimeMillis(),
            "email" to email,
            "name" to fullName,
            "disabled" to false
        )
//                    val userDetails = UserDetailsModel(
//                        createdAt = System.currentTimeMillis(),
//                        id = user.uid,
//                        email = email,
//                        name = fullName,
//                        disabled = false
//                    )

        val usersCollection = firestore.collection("users")
        try {
            usersCollection.document(userId).set(userDetails).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user details", e)
        }
    }


    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

    override fun logout() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "FirebaseAuthRepositoryI"
    }
}