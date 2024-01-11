package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.kitelytech.castlelink.R
import kotlinx.android.synthetic.main.layout_setting_row_header.view.*

@SuppressLint("ViewConstructor")
class SettingRowHeader constructor(
    context: Context,
    title: String
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_row_header, this, true)
        tvTitle.text = title
    }
}