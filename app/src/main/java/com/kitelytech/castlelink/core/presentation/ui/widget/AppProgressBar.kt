package com.kitelytech.castlelink.core.presentation.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.kitelytech.castlelink.databinding.LayoutAppProgressBarBinding

class AppProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutAppProgressBarBinding.inflate(LayoutInflater.from(context), this)

    init {
        setProgress(0)
    }

    fun setProgress(progress: Int) = with(binding) {
        progressBar.setProgress(progress, false)
        progressValue.text = "$progress %"
    }
}