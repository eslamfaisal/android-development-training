package com.training.ecommerce.ui.home.model

data class SalesAdUIModel(
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val endAt: Long? = null,
)

enum class SalesAdType {
    PRODUCT,
    CATEGORY,
    EXTERNAL_LINK,
    EMPTY
}