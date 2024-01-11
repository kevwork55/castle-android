package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.dialogs.DebugSheetDialog
import com.kitelytech.castlelink.custom.models.SettingGroup
import com.kitelytech.castlelink.custom.models.SettingValueSet
import kotlinx.android.synthetic.main.layout_product_action.view.*

@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables", "CutPasteId")
class ProductActionView constructor(
    context: Context,
    settingGroup: SettingGroup,
    valueSet: SettingValueSet,
    isSaved: Boolean
) : RelativeLayout(context) {
    private var isLocked : Boolean = isSaved
    private val settingGroup : SettingGroup
    private val valueSet: SettingValueSet
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_product_action, this, true)
        this.settingGroup = settingGroup
        this.valueSet = valueSet

        if (isLocked) {
            imgSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_locked))
        } else {
            imgSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unlocked))
        }

        btnSaveSettings.setOnClickListener {
            isLocked = !isLocked
            if (isLocked) {
                imgSave.setImageDrawable(context.getDrawable(R.drawable.ic_locked))
            } else {
                imgSave.setImageDrawable(context.getDrawable(R.drawable.ic_unlocked))
            }
        }

        btnSend.setOnClickListener {
            // Send to Device
            DebugSheetDialog(context, debugText()).show()
        }
    }

    private fun debugText() : String {
        var debugText = ""
        debugText += "Default settings:\r\n"
        debugText += settingGroup.defaults + "\r\n"

        val memory = valueSet.settingMemory()
        debugText += "\r\n"
        debugText += "Current settings:\r\n"
        debugText += if (memory?.hexString()?.isEmpty() == true) {
            "error" + "\r\n"
        } else {
            memory?.hexString() + "\r\n"
        }
        debugText += "\r\n"
        debugText += "Current values:\n"
        debugText += valueSet.dump().replaceIndent()
        debugText += "\r\n\r\n"
        debugText += "Memory map:\r\n"
        debugText += settingGroup.dumpMemoryMap().replaceIndent()
        return debugText
    }
}