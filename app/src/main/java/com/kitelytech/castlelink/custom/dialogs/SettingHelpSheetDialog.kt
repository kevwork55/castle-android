package com.kitelytech.castlelink.custom.dialogs

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.Product

class SettingHelpSheetDialog (context: Context, product: Product) : BottomSheetDialog(context){
    private var mContext: Context
    private var product: Product

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_setting_help_sheet)

        val linSettingHelp : LinearLayout? = findViewById(R.id.linSettingHelp)

        if (product.firmwareVersions.first().settingGroup != null) {
            for (subgroup in product.firmwareVersions.first().settingGroup!!.displaySubgroups()) {
                for (setting in subgroup.displaySettings()) {
                    if (setting.help != "" && setting.help != "na") {
                        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        params.setMargins(10, 10, 10, 10)

                        val tvTitle = TextView(context)
                        tvTitle.textSize = 12f
                        tvTitle.typeface = Typeface.DEFAULT_BOLD
                        tvTitle.setTextColor(context.getColor(R.color.colorWhite))
                        tvTitle.layoutParams = params
                        tvTitle.text = setting.title

                        val tvDescription = TextView(context)
                        tvDescription.textSize = 9f
                        tvDescription.setTextColor(context.getColor(R.color.colorWhite))
                        tvDescription.layoutParams = params
                        tvDescription.text = setting.help

                        linSettingHelp?.addView(tvTitle)
                        linSettingHelp?.addView(tvDescription)
                    }
                }
            }
        }
    }

    init {
        this.mContext = context
        this.product = product
    }
}