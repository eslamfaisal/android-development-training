package com.training.ecommerce.ui.auth.fragments

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.ecommerce.R
import com.training.ecommerce.databinding.FragmentCountriesBinding
import com.training.ecommerce.ui.auth.adapter.CountriesAdapter
import com.training.ecommerce.ui.auth.adapter.CountryClickListener
import com.training.ecommerce.ui.auth.models.CountryUIModel
import com.training.ecommerce.ui.auth.viewmodel.CountriesViewModel
import com.training.ecommerce.ui.common.fragments.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountriesFragment : BaseBottomSheetFragment<FragmentCountriesBinding, CountriesViewModel>(),
    CountryClickListener {

    override val viewModel: CountriesViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_countries

    override fun init() {
        initViewModel()
    }

    private fun initViewModel() {

        lifecycleScope.launch {
            viewModel.countriesUIModelState.collectLatest {
                if(it.isEmpty()) return@collectLatest
                binding.progressBar.visibility = View.GONE
                binding.countriesLayout.visibility = View.VISIBLE

                val countriesAdapter = CountriesAdapter(it, this@CountriesFragment)
                binding.countriesRv.apply {
                    adapter = countriesAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    override fun onCountryClicked(country: CountryUIModel) {
        viewModel.saveUserCountry(country)
        dismiss()
    }

}