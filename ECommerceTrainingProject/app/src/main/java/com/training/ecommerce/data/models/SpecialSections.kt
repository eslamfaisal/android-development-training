package com.training.ecommerce.data.models

enum class SpecialSections(val id: String) {
    RECOMMENDED_PRODUCTS(id = "recommended_products"),
}

enum class SpecialSectionsType {
    SINGLE_PRODUCT, RECOMMENDED_PRODUCTS, CATEGORY, CUSTOM_PRODUCTS,
}

data class SpecialSectionModel(
    val id: String? = null,
    val title: String?= null,
    val description: String?= null,
    val type: String?= null,
    val image: String?= null,
    val enabled: Boolean?= null,
) {
    override fun toString(): String {
        return "SpecialSectionModel(id=$id, title=$title, description=$description, type=$type, image=$image, enabled=$enabled)"
    }
}