package com.training.ecommerce.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.home.SalesAdsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val salesAdsRepository: SalesAdsRepository
) : ViewModel() {

    val salesAdsStateTemp = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    init {
    }


}