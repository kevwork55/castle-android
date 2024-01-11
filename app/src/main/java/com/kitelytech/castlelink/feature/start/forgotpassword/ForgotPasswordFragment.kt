package com.kitelytech.castlelink.feature.start.forgotpassword

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.common.ErrorCode
import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.util.ErrorParser
import com.kitelytech.castlelink.databinding.FragmentForgotPasswordBinding
import com.kitelytech.castlelink.feature.start.StartCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {
    override val viewModelKClass: KClass<ForgotPasswordViewModel>
        get() = ForgotPasswordViewModel::class

    override fun inflateBinding() =
        FragmentForgotPasswordBinding.inflate(LayoutInflater.from(requireContext()))

    private var forgotPasswordCallback: ForgotPasswordCallback? = null
    private var loadingCallback: StartCallback? = null

    override fun initialiseFragment() = with(binding) {
        setClickListeners()
        emailTied.addTextChangedListener(setupTextWatcher(emailTil))
        observeValidationError()
        Unit
    }

    private fun setClickListeners() = with(binding) {
        backIv.setOnClickListener { forgotPasswordCallback?.onCloseForgotPassword() }
        submitB.setOnClickListener { restorePassword() }
        emailTied.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                restorePassword()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setupTextWatcher(view: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                view.error = null
                view.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun restorePassword() = with(binding) {
        viewModel.restorePassword(
            emailTied.text.toString()
        )
    }

    private fun observeValidationError() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.validationErrorFlow.collectLatest {
                if (!it.isEmailValid || it.isEmailEmpty) {
                    val error = when {
                        it.isEmailEmpty -> getString(R.string.email_empty_error)
                        else -> getString(R.string.email_validation_error)
                    }
                    setEmailError(error)
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.requestStateFlow.collectLatest {
                when (it) {
                    is RequestState.Error -> {
                        val localizedMessaged =
                            ErrorParser.parseError(requireContext(), it.errorCode, it.message)
                        when(it.errorCode) {
                            ErrorCode.USER_DO_NOT_EXIST_ERROR -> {
                                setEmailError(localizedMessaged)
                            }
                            else -> {
                                showErrorDialog(getString(R.string.all_error_something_wrong_title), localizedMessaged)
                                hideKeyboard()
                            }
                        }

                        loadingCallback?.setLoadingState(false)
                    }
                    RequestState.Idle -> {
                        loadingCallback?.setLoadingState(false)
                    }
                    RequestState.Processing -> {
                        loadingCallback?.setLoadingState(true)
                    }
                    is RequestState.Success -> {
                        loadingCallback?.setLoadingState(false)
                        onSuccessResult()
                    }
                }
            }
        }
    }

    private fun onSuccessResult() = with(binding) {
        hideKeyboard(requireView())
        submitB.isVisible = false
        emailTil.isVisible = false
        instructionTv.text = getString(R.string.password_restore_check_email)
        headerIv.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_forgot_password_logo
            )
        )
    }

    private fun setEmailError(error: String) = with(binding) {
        emailTil.error = error
        emailTil.requestFocus()
        openKeyboard(emailTied)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeStatusBarColor(R.color.background_light)
        if (context is ForgotPasswordCallback) forgotPasswordCallback = context
        if (context is StartCallback) loadingCallback = context
    }

    override fun onDetach() {
        changeStatusBarColor(R.color.background_dark)
        super.onDetach()
        forgotPasswordCallback = null
        loadingCallback = null
    }

    companion object {
        val TAG = ForgotPasswordFragment::class.java.simpleName
        fun newInstance() = ForgotPasswordFragment()
    }
}