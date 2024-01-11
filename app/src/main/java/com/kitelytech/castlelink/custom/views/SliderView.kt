package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.ComponentSetting
import kotlinx.android.synthetic.main.layout_slider.view.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId")
class SliderView @JvmOverloads constructor(
    context: Context,
    component: ComponentSetting
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_slider, this, true)

        tvTitle.text = component.name
        if (component.valueType == "int") {
            seekBar.max = component.maxValue?.toInt()!!
            seekBar.min = component.minValue?.toInt()!!
            if (component.value.isNullOrEmpty()) {
                seekBar.progress = component.minValue?.toInt()!!
                tvSlider.text = component.minValue
            } else {
                seekBar.progress = component.value?.toInt()!!
                tvSlider.text = component.value
            }
        } else {
            seekBar.max = (component.maxValue?.toFloat()!! * 10).toInt()
            seekBar.min = (component.minValue?.toFloat()!! * 10).toInt()
            if (component.value.isNullOrEmpty()) {
                seekBar.progress = (component.minValue?.toFloat()!! * 10).toInt()
                tvSlider.text = component.minValue
            } else {
                seekBar.progress = component.value?.toInt()!!
                tvSlider.text = String.format("%.1f", component.value!!.toDouble()/10)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                component.value = progress.toString()
                if (component.valueType == "int") {
                    tvSlider.text = progress.toString()
                } else {
                    tvSlider.text = String.format("%.1f", progress.toDouble()/10)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do Nothing
            }
        })
    }
}
