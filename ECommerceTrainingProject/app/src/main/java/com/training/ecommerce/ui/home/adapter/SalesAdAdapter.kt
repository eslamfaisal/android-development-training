package com.training.ecommerce.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.training.ecommerce.databinding.ItemSalesAdBinding
import com.training.ecommerce.ui.home.model.SalesAdUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SalesAdAdapter(
    private val lifecycleScope: LifecycleCoroutineScope, private val salesAds: List<SalesAdUIModel>
) : RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {

    inner class SalesAdViewHolder(private val binding: ItemSalesAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: SalesAdUIModel) {
            salesAd.startCountdown()
            binding.lifecycleScope = lifecycleScope
            binding.salesAd = salesAd
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesAdViewHolder {
        val binding = ItemSalesAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalesAdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalesAdViewHolder, position: Int) {
        holder.bind(salesAds[position])
    }

    override fun getItemCount(): Int = salesAds.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        salesAds.forEach { it.stopCountdown() }
    }
}

@BindingAdapter("countdownTimer", "lifecycleScope")
fun timerChanges(
    view: TextView,
    timerState: MutableStateFlow<String>?,
    lifecycleScope: LifecycleCoroutineScope?
) {
    lifecycleScope?.launch {
        timerState?.collectLatest {
            view.text = it
        }
    }
}