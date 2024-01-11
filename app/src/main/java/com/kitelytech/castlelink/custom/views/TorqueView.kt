package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.ComponentSetting
import kotlinx.android.synthetic.main.layout_slider.view.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId", "SetTextI18n")
class TorqueView constructor(
    context: Context,
    component: ComponentSetting
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_torque, this, true)
        tvTitle.text = component.name
        seekBar.max = component.maxValue?.toInt()!!
        seekBar.min = component.minValue?.toInt()!!
        if (component.value.isNullOrEmpty()) {
            seekBar.progress = component.minValue?.toInt()!!
            tvSlider.text = component.minValue + "%"
        } else {
            seekBar.progress = component.value?.toInt()!!
            tvSlider.text = component.value  + "%"
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                component.value = progress.toString()
                tvSlider.text = progress.toString() + "%"
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