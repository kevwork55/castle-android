package com.kitelytech.castlelink.core.presentation.ui.widget.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kitelytech.castlelink.R

class MultiplySelectorAdapter(
    context: Context, resourceId: Int, data: List<String>
) : ArrayAdapter<String>(context, resourceId, data) {
    var selectedPosition = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val createdView = super.getView(position, convertView, parent)
        val backgroundResId = if (position == selectedPosition) {
            when (position) {
                0 -> R.drawable.gradient_background_main_left_round
                count.minus(1) -> R.drawable.gradient_background_main_right_round
                else -> R.drawable.gradient_background_main_straight
            }

        } else {
            when (position) {
                0 -> R.drawable.background_ms_left_rounded
                count.minus(1) -> R.drawable.background_ms_right_rounded
                else -> R.drawable.background_ms
            }
        }

        createdView.setBackgroundResource(backgroundResId)
        return createdView
    }
}