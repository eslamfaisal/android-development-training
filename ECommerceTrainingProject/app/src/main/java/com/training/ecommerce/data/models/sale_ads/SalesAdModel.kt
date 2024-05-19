package com.training.ecommerce.data.models.sale_ads

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SalesAdModel(
    val title: String? = null,
    val description: String? = null,

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String? = null,
    val type: String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId: String? = null,

    @get:PropertyName("category_id")
    @set:PropertyName("category_id")
    var categoryId: String? = null,

    @get:PropertyName("external_link")
    @set:PropertyName("external_link")
    var externalLink: String? = null,

    @ServerTimestamp
    @get:PropertyName("end_at")
    @set:PropertyName("end_at")
    var endAt: Long? = null
): Parcelable