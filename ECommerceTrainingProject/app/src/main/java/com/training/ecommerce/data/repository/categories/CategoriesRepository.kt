package com.training.ecommerce.data.repository.categories

import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.ui.home.model.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategories(): Flow<Resource<List<CategoryUIModel>>>
}