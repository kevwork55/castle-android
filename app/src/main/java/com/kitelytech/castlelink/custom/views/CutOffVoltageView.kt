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
import kotlinx.android.synthetic.main.layout_cutoff_voltage.view.*

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId", "SetTextI18n")
class CutOffVoltageView constructor(
    context: Context,
    component: ComponentSetting,
    screenWidth: Int
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_cutoff_voltage, this, true)
        tvTitle.text = component.name

        if (component.value.isNullOrEmpty() || component.value == "No Cutoff") {
            noCutOff()
        } else if (component.value == "Auto-Lipo"){
            autoLipo()
        } else {
            setVoltage()
        }

        tvNoCutOff.setOnClickListener {
            component.value = "No Cutoff"
            noCutOff()
        }

        tvSetVoltage.setOnClickListener {
            if (component.value.isNullOrEmpty() || component.value == "No Cutoff" || component.value == "Auto-Lipo") {
                component.value = component.options[0]
            }
            setVoltage()

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
        }

        tvAuto.setOnClickListener {
            component.value = "Auto-Lipo"
            autoLipo()
        }
    }

    private fun noCutOff() {
        tvNoCutOff.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_left)
        tvSetVoltage.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_center)
        tvAuto.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
        optionView.visibility = GONE
    }

    private fun setVoltage() {
        tvNoCutOff.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
        tvSetVoltage.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_center)
        tvAuto.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_right)
        optionView.visibility = VISIBLE
    }

    private fun autoLipo() {
        tvNoCutOff.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_left)
        tvSetVoltage.background = ContextCompat.getDrawable(context, R.drawable.bg_unselected_center)
        tvAuto.background = ContextCompat.getDrawable(context, R.drawable.bg_selected_right)
        optionView.visibility = GONE
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
            if (component.value == option) {
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
                component.value = optionTextView.text.toString()
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