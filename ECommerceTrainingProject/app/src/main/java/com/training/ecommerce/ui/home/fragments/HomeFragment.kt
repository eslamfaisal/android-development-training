package com.training.ecommerce.ui.home.fragments

import android.util.Log
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.training.ecommerce.R
import com.training.ecommerce.databinding.FragmentHomeBinding
import com.training.ecommerce.ui.common.fragments.BaseFragment
import com.training.ecommerce.ui.common.views.CircleView
import com.training.ecommerce.ui.home.adapter.SalesAdAdapter
import com.training.ecommerce.ui.home.model.SalesAdUIModel
import com.training.ecommerce.ui.home.viewmodel.HomeViewModel
import com.training.ecommerce.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
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
            viewModel.salesAdsStateTemp.collect {
                Log.d(TAG, "iniViewModel: $it")
            }
        }
    }

    private fun initViews() {
        Log.d(TAG, "onViewCreated: HomeFragment")

        initSalesAdsView()
    }

    private fun initSalesAdsView() {
        val salesAds = listOf(
            SalesAdUIModel(
                title = "Super Flash Sale",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/android-e-commerce-training.appspot.com/o/temps%2Fbig-sale-megaphone-banner-isolated-on-white-background-vector-sale-banner-discount-offer-market-advertising-illustration-2BNBMX2.jpg?alt=media&token=62fa2e9c-e40b-4e13-b0cc-76435e4e1c54",
                endAt = System.currentTimeMillis() + 3600000 // 1 hour from now
            ), SalesAdUIModel(
                title = "Limited Offer",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/android-e-commerce-training.appspot.com/o/temps%2FPromotion%20Image.png?alt=media&token=ae502706-b8f2-4e02-894f-6887d082d08a",
                endAt = System.currentTimeMillis() + 7200000 // 2 hours from now
            )
        )

        initializeIndicators(salesAds.size)
        val adapter = SalesAdAdapter(salesAds)
        binding.saleAdsViewPager.adapter = adapter
        binding.saleAdsViewPager.setPageTransformer(DepthPageTransformer())
        binding.saleAdsViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        lifecycleScope.launch(IO) {
            tickerFlow(5000).collect {
                withContext(Main) {
                    binding.saleAdsViewPager.setCurrentItem(
                        (binding.saleAdsViewPager.currentItem + 1) % salesAds.size, true
                    )
                }
            }
        }
    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private var indicators = mutableListOf<CircleView>()

    private fun initializeIndicators(count: Int) {
        for (i in 0 until count) {
            val circleView = CircleView(requireContext())
            val params = LinearLayout.LayoutParams(
                16, 16
            )
            params.setMargins(8, 0, 8, 0) // Margin between circles
            circleView.setLayoutParams(params)
            circleView.setRadius(8f) // Set radius
            circleView.setColor(
                if (i == 0) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            ) // First indicator is red
            circleView.setOnClickListener {
                binding.saleAdsViewPager.setCurrentItem(i, true)
            }
            indicators.add(circleView)
            binding.indicatorView.addView(circleView)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicators.size) {
            indicators[i].setColor(
                if (i == position) requireContext().getColor(R.color.primary_color) else requireContext().getColor(
                    R.color.neutral_grey
                )
            )
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}