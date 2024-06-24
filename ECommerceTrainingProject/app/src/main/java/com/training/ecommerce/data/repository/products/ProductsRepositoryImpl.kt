package com.training.ecommerce.data.repository.products

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
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
            val products = firestore.collection("products")
                .whereEqualTo("sale_type", saleType)
                .orderBy("price")
                .limit(pageLimit.toLong()).get().await().toObjects(ProductModel::class.java)
            emit(products)
        }
    }
}