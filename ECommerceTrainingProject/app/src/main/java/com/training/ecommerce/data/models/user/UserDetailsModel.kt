package com.training.ecommerce.data.models.user

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserDetailsModel(

    @get:PropertyName("created-at")
    @set:PropertyName("created-at")
    var createdAt: Long? = null,
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var reviews: List<String>? = null,
) : Parcelable
