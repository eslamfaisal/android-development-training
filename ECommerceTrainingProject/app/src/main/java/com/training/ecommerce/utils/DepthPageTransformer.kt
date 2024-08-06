package com.training.ecommerce.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> {
                view.alpha = 0f
            }
            position <= 0 -> {
                view.alpha = 1f
                view.translationX = 0f
                view.translationZ = 0f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            position <= 1 -> {
                view.alpha = 1 - position
                view.translationX = view.width * -position
                view.translationZ = -1f
                val scaleFactor = 0.75f + (1 - Math.abs(position)) * 0.25f
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            else -> {
                view.alpha = 0f
            }
        }
    }
}
