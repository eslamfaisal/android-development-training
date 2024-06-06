package com.training.ecommerce.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.products.ProductSaleType
import com.training.ecommerce.data.repository.categories.CategoriesRepository
import com.training.ecommerce.data.repository.home.SalesAdsRepository
import com.training.ecommerce.data.repository.products.ProductsRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdsRepository: SalesAdsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }

    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }
    }

    fun getFlashSaleProducts() = viewModelScope.launch(IO) {
        val country = userPreferenceRepository.getUserCountry().first()
        productsRepository.getSaleProducts(
            country.id, ProductSaleType.FLASH_SALE.type, 10
        ).collectLatest { products ->
            Log.d(TAG, "Flash sale products: $products")
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}