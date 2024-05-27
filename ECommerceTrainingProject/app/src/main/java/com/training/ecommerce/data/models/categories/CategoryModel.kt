package com.training.ecommerce.data.models.categories

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CategoryModel(
    val id: String? = null,
    val name: String? = null,
    val icon: String? = null,
) : Parcelable {
    override fun toString(): String {

        return "id = ${id}, name = $name"
    }
}