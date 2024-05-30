package com.training.ecommerce.data.models.products

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,

    @get:PropertyName("categories_ids")
    @set:PropertyName("categories_ids")
    var categoriesIDs: List<String>? = null,

    var images: List<String>? = null,

    var prices: List<ProductPriceModel>? = null,

    @get:PropertyName("has_offer")
    @set:PropertyName("has_offer")
    var hasOffer: Boolean? = null,

    @get:PropertyName("offer_percentage")
    @set:PropertyName("offer_percentage")
    var offerPercentage: Int? = null,

    var colors: List<String>? = null
) : Parcelable {

    fun getPrice(countryID: String): Float {
      return  prices?.find { it.countryId == countryID }?.price ?:0f

    }

}

@Keep
@Parcelize
data class ProductPriceModel (
    @get:PropertyName("country_id")
    @set:PropertyName("country_id")
    var countryId: String? = null,
    var price: Float? = null,
) : Parcelable