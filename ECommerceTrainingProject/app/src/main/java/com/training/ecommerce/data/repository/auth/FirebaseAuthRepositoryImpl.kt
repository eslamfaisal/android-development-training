package com.training.ecommerce.data.repository.auth

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.datasource.networking.CloudFunctionAPI
import com.training.ecommerce.data.datasource.networking.handleErrorResponse
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.auth.RegisterRequestModel
import com.training.ecommerce.data.models.auth.RegisterResponseModel
import com.training.ecommerce.data.models.user.AuthProvider
import com.training.ecommerce.data.models.user.UserDetailsModel
import com.training.ecommerce.utils.CrashlyticsUtils
import com.training.ecommerce.utils.LoginException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudFunctionAPI: CloudFunctionAPI
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

            if (authResult.user?.isEmailVerified == false) {
                authResult.user?.sendEmailVerification()?.await()
                val msg = "Email not verified, verification email sent to user"
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

    override suspend fun registerWithFacebookWithAPI(token: String): Flow<Resource<RegisterResponseModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = cloudFunctionAPI.registerWithSocialMedia(token,"facebook")
                if(response.isSuccessful){
                    val registerResponse = response.body()
                    registerResponse?.data?.let {
                        emit(Resource.Success(it))
                    }?:run {
                        emit(Resource.Error(Exception(registerResponse?.message)))
                    }
                }else {
                    emit(Resource.Error(Exception(handleErrorResponse(response.errorBody()!!.charStream()))))
                }
            }catch (e:Exception){
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun registerWithGoogleWithAPI(idToken: String): Flow<Resource<RegisterResponseModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = cloudFunctionAPI.registerWithSocialMedia(idToken,"google")
                if(response.isSuccessful){
                    val registerResponse = response.body()
                    registerResponse?.data?.let {
                        emit(Resource.Success(it))
                    }?:run {
                        emit(Resource.Error(Exception(registerResponse?.message)))
                    }
                }else {
                    emit(Resource.Error(Exception(handleErrorResponse(response.errorBody()!!.charStream()))))
                }
            }catch (e:Exception){
                emit(Resource.Error(e))
            }
        }
    }

    override suspend fun registerEmailAndPasswordWithAPI(registerRequestModel: RegisterRequestModel): Flow<Resource<RegisterResponseModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = cloudFunctionAPI.registerUser(registerRequestModel)
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    registerResponse?.data?.let {
                        emit(Resource.Success(it))
                    } ?: run {
                        emit(Resource.Error(Exception(registerResponse?.message)))
                    }
                } else {
                    Log.d(
                        TAG,
                        "registerEmailAndPasswordWithAPI: Error registering user = ${response.errorBody()}"
                    )
                    emit(Resource.Error(Exception(handleErrorResponse(response.errorBody()!!.charStream()))))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }


    override suspend fun registerWithGoogle(idToken: String): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                // perform firebase auth sign up request
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                // create user details object
                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                )
                // save user details to firestore
                firestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.GOOGLE.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    override suspend fun registerWithFacebook(token: String): Flow<Resource<UserDetailsModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                // perform firebase auth sign up request
                val credential = FacebookAuthProvider.getCredential(token)
                val authResult = auth.signInWithCredential(credential).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.FACEBOOK.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                // create user details object
                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = authResult.user?.email ?: "",
                )
                // save user details to firestore
                firestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.FACEBOOK.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    override suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>> {
        return flow {
            try {
                emit(Resource.Loading())
                auth.sendPasswordResetEmail(email).await()
                emit(Resource.Success("Password reset email sent"))
            } catch (e: Exception) {
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    override suspend fun registerWithEmailAndPassword(
        name: String, email: String, password: String
    ): Flow<Resource<UserDetailsModel>> {

        return flow {
            try {
                emit(Resource.Loading())
                // perform firebase auth sign up request
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AuthProvider.EMAIL.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                // create user details object
                val userDetails = UserDetailsModel(
                    id = userId, name = name, email = email
                )
                // save user details to firestore
                firestore.collection("users").document(userId).set(userDetails).await()
                authResult?.user?.sendEmailVerification()?.await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AuthProvider.EMAIL.name
                )
                emit(Resource.Error(e)) // Emit error
            }
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