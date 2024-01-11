package com.kitelytech.castlelink.core.presentation.ui.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import com.kitelytech.castlelink.core.presentation.dialog.error.SimpleErrorDialogFragment
import com.kitelytech.castlelink.core.presentation.extension.baseViewModels
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.BaseDialogFragment
import com.kitelytech.castlelink.core.presentation.ui.base.dialog.type.DialogType
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import kotlin.reflect.KClass


abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel> : Fragment(),
    BaseDialogFragment.OnDialogClick {
    private var _binding: Binding? = null
    protected val binding: Binding
        get() = checkNotNull(_binding)

    open var viewModelStoreOwner: () -> ViewModelStore = { viewModelStore }

    abstract val viewModelKClass: KClass<ViewModel>
    val viewModel: ViewModel by baseViewModels()

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
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!onBackPressed()) {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })
        initialiseFragment()
    }

    protected open fun initialiseFragment() {

    }

    protected open fun showErrorDialog(
        title: String,
        body: String
    ) {
        SimpleErrorDialogFragment.newInstance(title, body)
            .show(childFragmentManager, SimpleErrorDialogFragment.TAG)
    }

    protected fun openKeyboard(view: View) {
        val imm: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    protected fun hideKeyboard(view: View = requireView()) {
        val imm: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun changeStatusBarColor(color: Int) {
        val window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

    protected open fun onBackPressed(): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDialogConfirmClick(
        dialog: BaseDialogFragment<*, *>,
        data: Any?,
        dialogType: DialogType
    ) {

    }

    override fun onDialogCancelClick(
        dialog: BaseDialogFragment<*, *>,
        data: Any?,
        dialogType: DialogType
    ) {

    }
}