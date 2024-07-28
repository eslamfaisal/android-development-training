package com.training.ecommerce.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.training.ecommerce.databinding.ItemProductImageLayoutBinding

class ProductImagesAdapter(
    private val productImages: List<String>
) : RecyclerView.Adapter<ProductImagesAdapter.ProductImageViewHolder>() {

    inner class ProductImageViewHolder(private val binding: ItemProductImageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(salesAd: String) {
            binding.imageUrl = salesAd
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = ItemProductImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(productImages[position])
    }

    override fun getItemCount(): Int = productImages.size

}