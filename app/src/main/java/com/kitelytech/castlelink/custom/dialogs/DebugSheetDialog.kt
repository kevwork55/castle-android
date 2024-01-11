package com.kitelytech.castlelink.custom.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kitelytech.castlelink.R


class DebugSheetDialog(context: Context, debugText: String) : BottomSheetDialog(context) {
    private var mContext: Context
    private var debugText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_debug_sheet)

        val btnCopy : TextView? = findViewById(R.id.btnCopy)
        btnCopy?.setOnClickListener {
            val debugClipboard: ClipboardManager = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val debugClip: ClipData = ClipData.newPlainText("DebugText", debugText)
            debugClipboard.setPrimaryClip(debugClip)

        }
        val tvDebug : TextView? = findViewById(R.id.tvDebug)
        tvDebug?.text = debugText
    }

    init {
        this.mContext = context
        this.debugText = debugText
    }
}