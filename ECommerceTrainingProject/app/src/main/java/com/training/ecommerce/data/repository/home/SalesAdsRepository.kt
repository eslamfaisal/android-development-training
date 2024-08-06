package com.training.ecommerce.data.repository.home

import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.sale_ads.SalesAdModel
import com.training.ecommerce.ui.home.model.SalesAdUIModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdUIModel>>>
}