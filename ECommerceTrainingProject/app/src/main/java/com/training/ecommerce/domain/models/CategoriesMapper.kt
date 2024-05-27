package com.training.ecommerce.domain.models

import com.training.ecommerce.data.models.categories.CategoryModel
import com.training.ecommerce.ui.home.model.CategoryUIModel


fun CategoryModel.toUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id, name = name, icon = icon
    )
}