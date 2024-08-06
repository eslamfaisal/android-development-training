package com.training.ecommerce.ui.common.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt


class CircleView : View {
    private var paint: Paint? = null
    private var color = 0
    private var radius = 0f

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        paint = Paint()
        paint!!.isAntiAlias = true
        color = -0x1000000 // Default color is black
        radius = 16f // Default radius is 16 pixels
        paint!!.color = color
    }

    fun setColor(@ColorInt color: Int) {
        this.color = color
        paint!!.color = color
        invalidate()
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2
        val cy = height / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius, paint!!)
    }
}
