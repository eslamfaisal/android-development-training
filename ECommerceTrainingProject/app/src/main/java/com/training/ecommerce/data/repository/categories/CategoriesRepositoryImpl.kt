package com.training.ecommerce.data.repository.categories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.categories.CategoryModel
import com.training.ecommerce.domain.models.toUIModel
import com.training.ecommerce.ui.home.model.CategoryUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CategoriesRepository {
    override fun getCategories(): Flow<Resource<List<CategoryUIModel>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val categories = firestore.collection("categories").get().await()
                    .toObjects(CategoryModel::class.java)
                Log.d(TAG, "categories = ${categories}")

                // repeat categories item 10 times
//                val repeatCategories = mutableListOf<CategoryModel>()
//                repeat(10) {
//                    repeatCategories.addAll(categories)
//                }

                emit(Resource.Success(categories.map { it.toUIModel() }))
            } catch (e: Exception) {
                Log.e(TAG, "getCategories: error", e)
                emit(Resource.Error(e))
            }
        }
    }

    companion object {
        private const val TAG = "CategoriesRepositoryImp"
    }
}