package com.kitelytech.castlelink.feature.start.auth.login

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.common.ErrorCode
import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.util.ErrorParser
import com.kitelytech.castlelink.custom.activities.MainActivity
import com.kitelytech.castlelink.databinding.FragmentLoginBinding
import com.kitelytech.castlelink.feature.start.StartCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModelKClass: KClass<LoginViewModel>
        get() = LoginViewModel::class

    override fun inflateBinding() =
        FragmentLoginBinding.inflate(LayoutInflater.from(requireContext()))

    private var authCallback: LoginCallback? = null
    private var startCallback: StartCallback? = null

    override fun initialiseFragment() = with(binding) {
        setClickListeners()
        emailTied.addTextChangedListener(setupTextWatcher(emailTil))
        passwordTied.addTextChangedListener(setupTextWatcher(passwordTil))
        forgotPasswordTv.setOnClickListener { authCallback?.showForgotPassword() }
        observeValidationError()
        Unit
    }

    private fun setClickListeners() = with(binding) {
        loginB.setOnClickListener { login() }
        passwordTied.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                login()
                return@setOnEditorActionListener true
            }
            false
        }
        demoModeTv.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
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

    private fun login() = with(binding) {
        viewModel.loginUser(
            emailTied.text.toString(),
            passwordTied.text.toString()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginCallback) authCallback = context
        if (context is StartCallback) startCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        authCallback = null
        startCallback = null
    }

    private fun observeValidationError() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.validationErrorFlow.collectLatest {
                val isEmailValid = it.isEmailValid && !it.isEmailEmpty
                if (!isEmailValid) {
                    val error = when {
                        it.isEmailEmpty -> getString(R.string.email_empty_error)
                        else -> getString(R.string.email_validation_error)
                    }
                    setEmailError(error)
                }
                if (it.isPasswordEmpty) {
                    passwordTil.error = getString(R.string.password_empty_error)
                    if (isEmailValid) {
                        passwordTil.requestFocus()
                        openKeyboard(passwordTied)
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.requestStateFlow.collectLatest {
                when (it) {
                    is RequestState.Error -> {

                        when (it.errorCode) {
                            ErrorCode.INVALID_CREDENTIALS_ERROR, ErrorCode.USER_DO_NOT_EXIST_ERROR -> {
                                setEmailError(requireContext().getString(R.string.login_invalid_credentials))
                            }
                            else -> {
                                val localizedMessaged =
                                    ErrorParser.parseError(requireContext(), it.errorCode, it.message)
                                hideKeyboard()
                                showErrorDialog(
                                    getString(R.string.all_error_something_wrong_title),
                                    localizedMessaged
                                )
                            }
                        }
                        startCallback?.setLoadingState(false)
                    }
                    RequestState.Idle -> {
                        startCallback?.setLoadingState(false)
                    }
                    RequestState.Processing -> {
                        startCallback?.setLoadingState(true)
                    }
                    is RequestState.Success -> {
                        hideKeyboard()
                        startCallback?.startHomeScreen()
                        startCallback?.setLoadingState(false)
                    }
                }
            }
        }
    }

    private fun setEmailError(error: String) = with(binding) {
        emailTil.error = error
        emailTil.requestFocus()
        openKeyboard(emailTied)
    }

    companion object {
        val TAG = LoginFragment::class.java.simpleName
        fun newInstance() = LoginFragment()
    }
}