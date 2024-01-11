package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.Setting
import kotlinx.android.synthetic.main.layout_setting_row_switch.view.*

@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "UseSwitchCompatOrMaterialCode", "SetTextI18n")
class SettingRowSwitch constructor(
    context: Context,
    setting: Setting
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_row_switch, this, true)

        tvTitle.text = setting.title
        tvSwitch.text = "Disabled"
        switchToggle.isChecked = false

        switchToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvSwitch.text = "Enabled"
            } else {
                tvSwitch.text = "Disabled"
            }
        }
    }
}