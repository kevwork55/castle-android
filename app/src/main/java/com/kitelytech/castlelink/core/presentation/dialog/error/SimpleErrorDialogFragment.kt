package com.kitelytech.castlelink.core.presentation.dialog.error

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.BaseDialogFragment
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.type.DialogType
import com.kitelytech.castlelink.databinding.DialogSimpleErrorBinding

class SimpleErrorDialogFragment :
    BaseDialogFragment<DialogSimpleErrorBinding, DialogType.SimpleError>() {

    override fun inflateBinding() =
        DialogSimpleErrorBinding.inflate(LayoutInflater.from(requireContext()))


    override fun configureView(savedInstanceState: Bundle?) = with(binding) {
        val title = requireArguments().getString(SIMPLE_ERROR_DIALOG_TITLE_EXTRA_KEY,"")
        val body = requireArguments().getString(SIMPLE_ERROR_DIALOG_BODY_EXTRA_KEY,"")
        errorDialogTitleTv.text = title
        errorDialogBodyTv.text = body
        errorDialogOkB.setOnClickListener { dismiss() }
    }

    companion object {
        const val SIMPLE_ERROR_DIALOG_TITLE_EXTRA_KEY = "SIMPLE_ERROR_DIALOG_TITLE_EXTRA_KEY"
        const val SIMPLE_ERROR_DIALOG_BODY_EXTRA_KEY = "SIMPLE_ERROR_DIALOG_BODY_EXTRA_KEY"

        val TAG = SimpleErrorDialogFragment::class.java.simpleName

        fun newInstance(
            title: String,
            body: String
        ) = SimpleErrorDialogFragment().apply {
            arguments = bundleOf(
                SIMPLE_ERROR_DIALOG_TITLE_EXTRA_KEY to title,
                SIMPLE_ERROR_DIALOG_BODY_EXTRA_KEY to body,
            )
        }
    }
}