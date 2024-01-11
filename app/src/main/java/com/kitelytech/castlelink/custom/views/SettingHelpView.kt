package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.dialogs.SettingHelpSheetDialog
import com.kitelytech.castlelink.custom.models.Product
import kotlinx.android.synthetic.main.layout_setting_help.view.*

@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId")
class SettingHelpView constructor(
    context: Context,
    product: Product
) : RelativeLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_help, this, true)
        btnSettingsDescription.setOnClickListener {
           // Go to Settings Descriptions
            SettingHelpSheetDialog(context, product).show()
        }
    }
}