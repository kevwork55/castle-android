package com.kitelytech.castlelink.core.presentation.ui.widget

import android.content.Context
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

class AppSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var seekbarListener: OnSeekBarChangeListener? = null

    init {
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                isActivated = progress == 0
                seekbarListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekbarListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekbarListener?.onStopTrackingTouch(seekBar)
            }
        })

    }

    fun setOnAppSeekBarChangeListener(listener: OnSeekBarChangeListener) {
        seekbarListener = listener
    }
}