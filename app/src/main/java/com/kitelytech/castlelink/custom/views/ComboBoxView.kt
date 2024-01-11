package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.ComponentSetting
import kotlinx.android.synthetic.main.layout_combo_box.view.*

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId")
class ComboBoxView constructor(
    context: Context,
    component: ComponentSetting,
    screenWidth: Int
) : RelativeLayout(context) {
    var isExpanded : Boolean = false
    var isVolt: Boolean = false
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_combo_box, this, true)

        tvTitle.text = component.name
        if (component.value.isNullOrEmpty()) {
            tvValue.text = component.options[0]
        } else {
            tvValue.text = component.value
        }

        isVolt = tvValue.text.toString().contains("Volts/Cell")
        if (isVolt) {
            tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
            linVolt.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
            linVolt.visibility = VISIBLE
        } else {
            tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_selected)
            linVolt.visibility = GONE
        }

        var tempWidth = 0
        for (option in component.options) {
            val optionTextView = TextView(context)
            optionTextView.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (35 * resources.displayMetrics.scaledDensity).toInt())
            optionTextView.gravity = Gravity.CENTER
            optionTextView.text = option
            optionTextView.textSize = 12.0f
            optionTextView.setTextColor(context.getColor(R.color.colorWhite))
            optionTextView.setPadding((8 * resources.displayMetrics.scaledDensity).toInt(), 0,
                (8 * resources.displayMetrics.scaledDensity).toInt(), 0)
            optionTextView.measure(0,0)
            if (tempWidth < optionTextView.measuredWidth) {
                tempWidth = optionTextView.measuredWidth
            }
        }

        addOptionsView(tempWidth, component, screenWidth)
        optionView.visibility = GONE

        tvValue.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                optionView.visibility = VISIBLE
                if (isVolt) {
                    tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
                    linVolt.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
                } else {
                    tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected)
                }
            } else {
                optionView.visibility = GONE
                if (isVolt) {
                    tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
                    linVolt.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
                } else {
                    tvValue.background = ContextCompat.getDrawable(context, R.drawable.bg_selected)
                }
            }
        }
    }

    private fun addOptionsView(tempWidth : Int, component : ComponentSetting, screenWidth : Int) {
        optionView.removeAllViews()
        var subLayout = LinearLayout(context)
        var totalWidth = 0
        var measuredWidth = tempWidth
        for (option in component.options) {
            measuredWidth += tempWidth
            totalWidth += tempWidth
            subLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            subLayout.setPadding(0, (4 * resources.displayMetrics.scaledDensity).toInt(),
                0, (4 * resources.displayMetrics.scaledDensity).toInt())
            subLayout.orientation = LinearLayout.HORIZONTAL
            val optionTextView = TextView(context)
            optionTextView.layoutParams = LayoutParams(tempWidth, (35 * resources.displayMetrics.scaledDensity).toInt())
            optionTextView.gravity = Gravity.CENTER
            optionTextView.text = option
            optionTextView.textSize = 12.0f
            optionTextView.setTextColor(context.getColor(R.color.colorWhite))
            optionTextView.setPadding((8 * resources.displayMetrics.scaledDensity).toInt(), 0,
                (8 * resources.displayMetrics.scaledDensity).toInt(), 0)
            if (tvValue.text == option) {
                if (measuredWidth == 2 * tempWidth) {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
                    if (measuredWidth > screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
                        optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected)
                    }
                } else if (measuredWidth < screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_center)
                    if (option == component.options.last()) {
                        optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
                    }
                } else {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
                }
            } else {
                if (measuredWidth == 2 * tempWidth) {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
                    if (measuredWidth > screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
                        optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected)
                    }
                } else if (measuredWidth < screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_center)
                    if (option == component.options.last()) {
                        optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
                    }
                } else {
                    optionTextView.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
                }
            }
            optionTextView.setOnClickListener {
                tvValue.text = optionTextView.text
                component.value = tvValue.text.toString()
                addOptionsView(tempWidth, component, screenWidth)
            }
            subLayout.addView(optionTextView)

            if (measuredWidth > screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
                optionView.addView(subLayout)
                subLayout = LinearLayout(context)
                totalWidth = 0
                measuredWidth = tempWidth
            }
        }

        if (totalWidth < screenWidth - (40 * resources.displayMetrics.scaledDensity).toInt()) {
            optionView.addView(subLayout)
        }
    }

}