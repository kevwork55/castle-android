package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.Setting
import kotlinx.android.synthetic.main.layout_setting_row.view.*

@SuppressLint("ViewConstructor")
class SettingRow constructor(
    context: Context,
    setting: Setting
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_row, this, true)
        tvTitle.text = setting.title
    }
}