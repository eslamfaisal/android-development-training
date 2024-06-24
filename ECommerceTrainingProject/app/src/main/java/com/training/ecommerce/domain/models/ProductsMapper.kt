package com.training.ecommerce.domain.models

import com.training.ecommerce.data.models.products.ProductModel
import com.training.ecommerce.ui.products.model.ProductUIModel


fun ProductUIModel.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        name = name,
        description = description,
        categoriesIDs = categoriesIDs,
        images = images,
        price = price,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors
    )
}

fun ProductModel.toProductUIModel(): ProductUIModel {
    return ProductUIModel(id = id ?: throw IllegalArgumentException("Product ID is missing"),
        name = name ?: "No Name",
        description = description ?: "No Description",
        categoriesIDs = categoriesIDs ?: emptyList(),
        images = images ?: emptyList(),
        price = price ?: 0,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors ?: emptyList())
}

