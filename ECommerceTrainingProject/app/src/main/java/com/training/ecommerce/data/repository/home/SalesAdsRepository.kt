package com.training.ecommerce.data.repository.home

import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.sale_ads.SalesAdModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdModel>>>
}