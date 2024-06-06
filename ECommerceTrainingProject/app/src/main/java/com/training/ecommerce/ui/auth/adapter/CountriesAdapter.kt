package com.training.ecommerce.ui.auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.training.ecommerce.databinding.CountryItemLayoutBinding
import com.training.ecommerce.ui.auth.models.CountryUIModel
import com.training.ecommerce.ui.common.views.getGlideCircleLoading

class CountriesAdapter(
    private val countries: List<CountryUIModel>,
    private val countryClickListener: CountryClickListener
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(private val binding: CountryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CountryUIModel) {
            binding.country = category
            binding.root.setOnClickListener {
                countryClickListener.onCountryClicked(category)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

}

interface CountryClickListener {
    fun onCountryClicked(country: CountryUIModel)
}