package com.training.ecommerce.ui.products.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.training.ecommerce.R
import com.training.ecommerce.databinding.FragmentProductDetailsBinding
import com.training.ecommerce.databinding.ProductItemLayoutBinding
import com.training.ecommerce.ui.common.fragments.BaseFragment
import com.training.ecommerce.ui.common.views.CircleView
import com.training.ecommerce.ui.common.views.sliderIndicatorsView
import com.training.ecommerce.ui.common.views.updateIndicators
import com.training.ecommerce.ui.products.adapter.ProductImagesAdapter
import com.training.ecommerce.ui.products.model.ProductUIModel
import com.training.ecommerce.ui.products.viewmodel.ProductDetailsViewModel
import com.training.ecommerce.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>() {

    override val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_product_details

    override fun init() {
      initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
                initView(it)
            }
        }
    }

    private fun initView(it: ProductUIModel) {
        it.name.let { binding.titleTv.text = it }
        initImagesView(it.images)
    }

    private var indicators = mutableListOf<CircleView>()
    private fun initImagesView(images: List<String>) {

        sliderIndicatorsView(
            requireContext(),
            binding.productImagesViewPager,
            binding.indicatorView,
            indicators,
            images.size
        )
        binding.productImagesViewPager.apply {
            adapter = ProductImagesAdapter(images)
            setPageTransformer(DepthPageTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }
    }

    companion object
}