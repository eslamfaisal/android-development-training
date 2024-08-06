package com.training.ecommerce.data.repository.products

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.products.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>>

    fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>>

    suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot? = null
    ): Flow<Resource<QuerySnapshot>>

    fun listenToProductDetails(productID: String): Flow<ProductModel>
}