package com.training.ecommerce.ui.home.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.products.ProductModel
import com.training.ecommerce.data.models.products.ProductSaleType
import com.training.ecommerce.data.models.user.CountryData
import com.training.ecommerce.data.repository.categories.CategoriesRepository
import com.training.ecommerce.data.repository.home.SalesAdsRepository
import com.training.ecommerce.data.repository.products.ProductsRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import com.training.ecommerce.domain.models.toProductUIModel
import com.training.ecommerce.ui.products.model.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@BindingAdapter("android:visibilities")
fun setVisibility(view: View, isEmpty: Boolean) {
    view.visibility = if (isEmpty) View.GONE else View.VISIBLE
}

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

    val countryState = userPreferenceRepository.getUserCountry().stateIn(
        viewModelScope + IO,
        started = SharingStarted.Eagerly,
        initialValue = CountryData.getDefaultInstance()
    )

    val flashSaleState = getProductsSales(ProductSaleType.FLASH_SALE)

    val megaSaleState = getProductsSales(ProductSaleType.MEGA_SALE)


    val isEmptyFlashSale: LiveData<Boolean> = flashSaleState.map {
        val isEmpty = it.isEmpty()
        Log.d(TAG, "isEmptyFlashSale: $isEmpty")
        isEmpty
    }.asLiveData()

    val isEmptyMegaSale: LiveData<Boolean> = megaSaleState.map {
        val isEmpty = it.isEmpty()
        Log.d(TAG, "isEmptyMegaSale: $isEmpty")
        isEmpty
    }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getProductsSales(productSaleType: ProductSaleType): StateFlow<List<ProductUIModel>> =
        countryState.mapLatest {
            Log.d(TAG, "Countryid for flah sale: ${it.id}")
            productsRepository.getSaleProducts(it.id ?: "0", productSaleType.type, 10)
        }.mapLatest { it.first().map { getProductModel(it) } }.stateIn(
            viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = emptyList()
        )


    private fun getProductModel(product: ProductModel): ProductUIModel {
        val productUIModel = product.toProductUIModel().copy(
            currencySymbol = countryState.value?.currencySymbol ?: ""
        )
        return productUIModel
    }

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

//@BindingAdapter("isVisible")
//fun setVisibility(view: View, isEmpty: Boolean) {
//    Log.d("HomeViewModel", "tage = ${view.tag}, setVisibility: $isEmpty")
//    view.visibility = if (isEmpty) View.GONE else View.VISIBLE
//}
