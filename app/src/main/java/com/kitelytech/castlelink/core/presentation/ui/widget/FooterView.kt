package com.kitelytech.castlelink.core.presentation.ui.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.widget.LinearLayoutCompat
import com.kitelytech.castlelink.BuildConfig
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.databinding.LayoutAppProgressBarBinding
import com.kitelytech.castlelink.databinding.LayoutFooterViewBinding

class FooterView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding = LayoutFooterViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
        val buildVersion = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        binding.footerTextTv.text = String.format(context.getString(R.string.login_footer_text_button), "$versionName ($buildVersion)")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeight = MeasureSpec.makeMeasureSpec(heightMeasureSpec, UNSPECIFIED)
        super.onMeasure(widthMeasureSpec, newHeight)
    }
}