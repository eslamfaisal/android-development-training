package com.training.ecommerce.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.training.ecommerce.databinding.ItemSalesAdBinding
import com.training.ecommerce.ui.home.model.SalesAdUIModel

class SalesAdAdapter(private val salesAds: List<SalesAdUIModel>) :
    RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {

    inner class SalesAdViewHolder(private val binding: ItemSalesAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: SalesAdUIModel) {
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
}
