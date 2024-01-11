package com.kitelytech.castlelink.core.presentation.ui.base.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.BaseDialogFragment
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.type.DialogType
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.extension.baseViewModels
import kotlin.reflect.KClass

abstract class BaseActivity<Binding : ViewBinding, ViewModel : BaseViewModel> :
    AppCompatActivity(), BaseDialogFragment.OnDialogClick {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = checkNotNull(_binding)

    abstract val viewModelKClass: KClass<ViewModel>
    val viewModel: ViewModel by baseViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding()
        setContentView(binding.root)
        configureBinding(savedInstanceState)
    }

    protected open fun configureBinding(savedInstanceState: Bundle?) {}

    protected abstract fun inflateBinding(): Binding

    protected fun openKeyboard(view: View) {
        val imm: InputMethodManager? =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    protected fun hideKeyboard(view: View) {
        val imm: InputMethodManager? =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDialogConfirmClick(
        dialog: BaseDialogFragment<*, *>,
        data: Any?,
        dialogType: DialogType
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onDialogConfirmClick(
            dialog,
            data,
            dialogType
        )
    }

    override fun onDialogCancelClick(
        dialog: BaseDialogFragment<*, *>,
        data: Any?,
        dialogType: DialogType
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onDialogCancelClick(
            dialog,
            data,
            dialogType
        )
    }
}