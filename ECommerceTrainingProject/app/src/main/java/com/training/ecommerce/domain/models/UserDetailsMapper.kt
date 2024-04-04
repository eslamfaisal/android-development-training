package com.training.ecommerce.domain.models

import com.training.ecommerce.data.models.user.UserDetailsModel
import com.training.ecommerce.data.models.user.UserDetailsPreferences


fun UserDetailsPreferences.toUserDetailsModel(): UserDetailsModel {
    return UserDetailsModel(
        id = id,
        email = email,
        name = name,
        reviews = reviewsList
    )
}

fun UserDetailsModel.toUserDetailsPreferences(): UserDetailsPreferences {
    return UserDetailsPreferences.newBuilder()
        .setId(id)
        .setEmail(email)
        .setName(name)
        .addAllReviews(reviews?.toList() ?: emptyList())
        .build()
}