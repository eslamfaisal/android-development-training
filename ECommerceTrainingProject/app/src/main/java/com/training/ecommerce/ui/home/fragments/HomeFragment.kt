package com.training.ecommerce.ui.home.fragments

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.training.ecommerce.R
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.databinding.FragmentHomeBinding
import com.training.ecommerce.ui.common.fragments.BaseFragment
import com.training.ecommerce.ui.common.views.CircleView
import com.training.ecommerce.ui.common.views.loadImage
import com.training.ecommerce.ui.common.views.sliderIndicatorsView
import com.training.ecommerce.ui.common.views.updateIndicators
import com.training.ecommerce.ui.home.adapter.CategoriesAdapter
import com.training.ecommerce.ui.home.adapter.SalesAdAdapter
import com.training.ecommerce.ui.home.model.CategoryUIModel
import com.training.ecommerce.ui.home.model.SalesAdUIModel
import com.training.ecommerce.ui.home.model.SpecialSectionUIModel
import com.training.ecommerce.ui.home.viewmodel.HomeViewModel
import com.training.ecommerce.ui.products.ProductDetailsActivity
import com.training.ecommerce.ui.products.ProductDetailsActivity.Companion.PRODUCT_UI_MODEL_EXTRA
import com.training.ecommerce.ui.products.adapter.ProductAdapter
import com.training.ecommerce.ui.products.adapter.ProductViewType
import com.training.ecommerce.ui.products.model.ProductUIModel
import com.training.ecommerce.utils.DepthPageTransformer
import com.training.ecommerce.utils.GridSpacingItemDecoration
import com.training.ecommerce.utils.HorizontalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun init() {
        initViews()
        iniViewModel()
    }

    private fun iniViewModel() {
        lifecycleScope.launch {
            viewModel.salesAdsState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: Loading")
                    }

                    is Resource.Success -> {
                        binding.saleAdsShimmerView.root.stopShimmer()
                        binding.saleAdsShimmerView.root.visibility = View.GONE
                        initSalesAdsView(resources.data)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: Error")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.categoriesState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d(TAG, "iniViewModel: categories Loading")
                    }

                    is Resource.Success -> {
//                        binding.categoriesShimmerView.root.stopShimmer()
//                        binding.categoriesShimmerView.root.visibility = View.GONE
                        Log.d(TAG, "iniViewModel: categories Success = ${resources.data}")
                        initCategoriesView(resources.data)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "iniViewModel: categories Error")
                    }
                }
            }
        }

//        viewModel.getFlashSaleProducts()

        lifecycleScope.launch {
            viewModel.flashSaleState.collect { productsList ->
                flashSaleAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }
        lifecycleScope.launch {
            viewModel.megaSaleState.collect { productsList ->
                megaSaleAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }

        lifecycleScope.launch {
            viewModel.recommendedSectionDataState.collectLatest { recommendedSectionData ->
                Log.d(TAG, "Recommended section data: $recommendedSectionData")
                recommendedSectionData?.let {
                    setupRecommendedViewData(it)
                } ?: run {
                    Log.d(TAG, "Recommended section data is null")
//                    binding.recommendedProductLayout.visibility = View.GONE
                }
            }
        }

        viewModel.getNextProducts()
        lifecycleScope.launch {
            viewModel.allProductsState.collectLatest { productsList ->
                allProductsAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }
    }

    private fun setupRecommendedViewData(sectionData: SpecialSectionUIModel) {
        loadImage(binding.recommendedProductIv, sectionData.image)
        binding.recommendedProductTitleIv.text = sectionData.title
        binding.recommendedProductDescriptionIv.text = sectionData.description
        binding.recommendedProductLayout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Recommended Product Clicked, goto ${sectionData.type}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initCategoriesView(data: List<CategoryUIModel>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val categoriesAdapter = CategoriesAdapter(data)
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private val flashSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val megaSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val allProductsAdapter by lazy { ProductAdapter { goToProductDetails(it) } }

    private fun initViews() {
        binding.flashSaleProductsRv.apply {
            adapter = flashSaleAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            addItemDecoration(HorizontalSpaceItemDecoration(16))
        }
        binding.megaSaleProductsRv.apply {
            adapter = megaSaleAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            addItemDecoration(HorizontalSpaceItemDecoration(16))
        }
        binding.allProductsRv.apply {
            adapter = allProductsAdapter
            layoutManager = GridLayoutManager(
                requireContext(), 2
            )
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }
    }

    private fun initSalesAdsView(salesAds: List<SalesAdUIModel>?) {
        if (salesAds.isNullOrEmpty()) {
            return
        }

        sliderIndicatorsView(
            requireContext(),
            binding.saleAdsViewPager,
            binding.indicatorView,
            indicators,
            salesAds.size
        )

        val salesAdapter = SalesAdAdapter(lifecycleScope, salesAds)
        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }

        lifecycleScope.launch(IO) {
            tickerFlow(5000).collect {
                withContext(Main) {
                    binding.saleAdsViewPager.setCurrentItem(
                        (binding.saleAdsViewPager.currentItem + 1) % salesAds.size, true
                    )
                }
            }
        }

        // add animation from top to bottom
        binding.saleAdsViewPager.animate().translationY(0f).alpha(1f).setDuration(500).start()

    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private var indicators = mutableListOf<CircleView>()

    private fun goToProductDetails(product: ProductUIModel) {
        requireActivity().startActivity(Intent(
            requireActivity(), ProductDetailsActivity::class.java
        ).apply {
            putExtra(PRODUCT_UI_MODEL_EXTRA, product)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}