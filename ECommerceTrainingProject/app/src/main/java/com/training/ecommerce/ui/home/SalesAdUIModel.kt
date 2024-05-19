package com.training.ecommerce.ui.home

data class SalesAdUIModel(
    val title: String,
    val description: String,
    val imageUrl: String
)

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}