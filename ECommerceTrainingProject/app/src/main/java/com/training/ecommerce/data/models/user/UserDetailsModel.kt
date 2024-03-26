package com.training.ecommerce.data.models.user

data class UserDetailsModel(
    val id: String,
    val email: String,
    val name: String,
    val reviews: List<String>,
)