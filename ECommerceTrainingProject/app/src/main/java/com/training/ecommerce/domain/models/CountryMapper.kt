package com.training.ecommerce.domain.models

import com.training.ecommerce.data.models.auth.CountryModel
import com.training.ecommerce.ui.auth.models.CountryUIModel


fun CountryModel.toUIModel(): CountryUIModel {
    return CountryUIModel(
        id = id,
        name = name,
        code = code,
        currency = currency,
        image = image,
        currencySymbol = currencySymbol
    )
}