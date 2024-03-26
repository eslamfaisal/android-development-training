package com.training.ecommerce.ui.common.models

data class UserDetailsModel(
    val id: String,
    val email: String,
    val name: String,
    val reviews: List<String>,
)