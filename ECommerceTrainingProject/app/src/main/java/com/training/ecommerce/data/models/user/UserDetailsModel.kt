package com.training.ecommerce.data.models.user

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserDetailsModel(
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var reviews: List<String>? = null,
) : Parcelable
