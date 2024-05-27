package com.training.ecommerce.ui.home.model

import java.util.Date

data class SalesAdUIModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,

    var imageUrl: String? = null, val type: String? = null,

    var productId: String? = null,

    var categoryId: String? = null,

    var externalLink: String? = null,

    var endAt: Date? = null
)

enum class SalesAdType {
    PRODUCT, CATEGORY, EXTERNAL_LINK, EMPTY
}