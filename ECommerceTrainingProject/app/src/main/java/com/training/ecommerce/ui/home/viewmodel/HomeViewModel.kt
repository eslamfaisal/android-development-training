package com.training.ecommerce.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.products.ProductModel
import com.training.ecommerce.data.models.products.ProductSaleType
import com.training.ecommerce.data.models.user.CountryData
import com.training.ecommerce.data.repository.categories.CategoriesRepository
import com.training.ecommerce.data.repository.home.SalesAdsRepository
import com.training.ecommerce.data.repository.products.ProductsRepository
import com.training.ecommerce.data.repository.special_sections.SpecialSectionsRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import com.training.ecommerce.domain.models.toProductUIModel
import com.training.ecommerce.domain.models.toSpecialSectionUIModel
import com.training.ecommerce.ui.products.model.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdsRepository: SalesAdsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val specialSectionsRepository: SpecialSectionsRepository
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

    val isEmptyFlashSale = flashSaleState.map { it.isEmpty() }.asLiveData()

    val isEmptyMegaSale = megaSaleState.map { it.isEmpty() }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val recommendedSectionDataState = specialSectionsRepository.recommendProductsSection().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = null
    ).mapLatest { it?.toSpecialSectionUIModel() }

    val isRecommendedSection = recommendedSectionDataState.map { it == null }.asLiveData()

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

    private val _allProductsState: MutableStateFlow<List<ProductUIModel>> =
        MutableStateFlow(emptyList())
    val allProductsState = _allProductsState.asStateFlow()
    val isLoadingAllProducts = MutableStateFlow(false)
    val isFinishedLoadAllProducts = MutableStateFlow(false)
    var lastDocumentSnapshot: DocumentSnapshot? = null

    fun getNextProducts() = viewModelScope.launch(IO) {
        if (isFinishedLoadAllProducts.value) return@launch
        if (isLoadingAllProducts.value) return@launch
        isLoadingAllProducts.emit(true)

        val countryId = countryState.first().id ?: "0"
        productsRepository.getAllProductsPaging(countryId, 2, lastDocumentSnapshot)
            .collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        isLoadingAllProducts.emit(false)
                        resource.data?.let { docs ->
                            if (docs.isEmpty) {
                                isFinishedLoadAllProducts.emit(true)
                                return@collectLatest
                            } else {
                                lastDocumentSnapshot = docs.documents.lastOrNull()
                                val lstProducts = docs.toObjects(ProductModel::class.java)
                                    .map { getProductModel(it) }
                                _allProductsState.emit(_allProductsState.value + lstProducts)
                            }
                        }
                    }

                    is Resource.Error -> {
                        isLoadingAllProducts.emit(false)
                        Log.d(TAG, "getNextProducts: ${resource.exception?.message}")
                    }

                    is Resource.Loading -> {
                        isLoadingAllProducts.emit(true)
                    }
                }
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
