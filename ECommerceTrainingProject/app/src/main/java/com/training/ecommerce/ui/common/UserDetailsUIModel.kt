package com.training.ecommerce.ui.common

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Keep
@Parcelize
data class UserDetailsUIModel(
    var createdAt: Int? = null,
    var id: String? = null,
    var email: String? = null,
    var name: String? = null,
    var reviews: List<String>? = null,
) : Parcelable {
    val formattedCreatedAt: String
        get() = createdAt?.let {
            val date = Date(it.toLong())
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        } ?: ""
}
