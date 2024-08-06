package com.training.ecommerce.data.repository.home

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.sale_ads.SalesAdModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SalesAdsRepository {
    override  fun getSalesAds() = flow {
        try {
            Log.d(TAG, "getSalesAds: ")
            emit(Resource.Loading())
            val salesAds =
                firestore.collection("sales_ads")
                    .get().await().toObjects(SalesAdModel::class.java)

            emit(Resource.Success(salesAds.map { it.toUIModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    companion object {
        private const val TAG = "SalesAdsRepositoryImpl"
    }

    suspend fun getPAgingSales(){
        val salesAds =  firestore.collection("sales_ads").limit(10).get().await()


         val lstDocument = salesAds.documents.last()

        getNextPage(lstDocument)
    }

    suspend fun getNextPage(lastDocument: DocumentSnapshot){
        val salesAds =  firestore.collection("sales_ads").startAfter(lastDocument).limit(10).get().await()
    }
}