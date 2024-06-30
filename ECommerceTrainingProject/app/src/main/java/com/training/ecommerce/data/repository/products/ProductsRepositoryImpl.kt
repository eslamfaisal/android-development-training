package com.training.ecommerce.data.repository.products

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.products.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductsRepository {

    override fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>> {
        return flow {
            val products =
                firestore.collection("products").whereArrayContains("categories_ids", categoryID)
                    .limit(pageLimit.toLong()).get().await().toObjects(ProductModel::class.java)

            emit(products)
        }
    }

    override fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>> {
        return flow {
            Log.d("ProductsRepositoryImpl", "getSaleProducts: $countryID, $saleType")
            val products = firestore.collection("products").whereEqualTo("sale_type", saleType)
                .whereEqualTo("country_id", countryID).orderBy("price").limit(pageLimit.toLong())
                .get().await().toObjects(ProductModel::class.java)
            emit(products)
        }
    }

    override suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot?
    ) = flow<Resource<QuerySnapshot>> {
        try {
            emit(Resource.Loading())

            var firstQuery = firestore.collection("products").orderBy("price")

            if (lastDocument != null) {
                firstQuery = firstQuery.startAfter(lastDocument)
            }

            firstQuery = firstQuery.limit(pageLimit)

            val products = firstQuery.get().await()
            emit(Resource.Success(products))
        } catch (e: Exception) {
            Log.d(TAG, "getAllProductsPaging: ${e.message}")
            emit(Resource.Error(e))
        }
    }

    companion object {
        private const val TAG = "ProductsRepositoryImpl"
    }

}