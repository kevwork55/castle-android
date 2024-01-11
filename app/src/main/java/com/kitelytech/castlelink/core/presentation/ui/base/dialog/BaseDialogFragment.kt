package com.kitelytech.castlelink.core.presentation.ui.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.type.DialogType

abstract class BaseDialogFragment<Binding : ViewBinding, Type : DialogType> : DialogFragment() {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = checkNotNull(_binding)

    protected var onDialogClickListener: OnDialogClick? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflateBinding().apply { _binding = this }.root
    }

    protected abstract fun inflateBinding(): Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        configureView(savedInstanceState)
    }

    protected open fun configureView(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        setPercentSize()
        super.onResume()
    }

    protected open fun setPercentSize() {
        val width = (resources.displayMetrics.widthPixels * 0.925).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDialogClick) onDialogClickListener = context
    }

    override fun onDetach() {
        super.onDetach()
        onDialogClickListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnDialogClick {
        fun onDialogConfirmClick(
            dialog: BaseDialogFragment<*, *>,
            data: Any? = null,
            dialogType: DialogType
        )

        fun onDialogCancelClick(
            dialog: BaseDialogFragment<*, *>,
            data: Any? = null,
            dialogType: DialogType
        )
    }
}