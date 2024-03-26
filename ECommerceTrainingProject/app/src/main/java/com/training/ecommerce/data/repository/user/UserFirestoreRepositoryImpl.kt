package com.training.ecommerce.data.repository.user

import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserFirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserFirestoreRepository {

    override suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>> =
        callbackFlow {
            send(Resource.Loading())
            val documentPath = "users/$userId"
            val document = firestore.document(documentPath)
            val listener = document.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(Exception(error.message)))
                    close(error)
                    return@addSnapshotListener
                }
                value?.toObject(UserDetailsModel::class.java)?.let {
                    trySend(Resource.Success(it))
                }
            }
            awaitClose { listener.remove() }
        }

}