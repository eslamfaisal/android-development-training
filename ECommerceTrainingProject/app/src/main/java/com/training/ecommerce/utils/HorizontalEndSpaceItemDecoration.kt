package com.training.ecommerce.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
            outRect.right = horizontalSpaceWidth
        }
    }
}
