package com.kitelytech.castlelink.feature.start.auth.registration

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
import com.kitelytech.castlelink.core.common.ErrorCode.USER_ALREADY_EXIST_ERROR
import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.util.ErrorParser
import com.kitelytech.castlelink.custom.activities.MainActivity
import com.kitelytech.castlelink.databinding.FragmentRegistrationBinding
import com.kitelytech.castlelink.feature.start.StartCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>() {
    override val viewModelKClass: KClass<RegistrationViewModel>
        get() = RegistrationViewModel::class

    override fun inflateBinding() =
        FragmentRegistrationBinding.inflate(LayoutInflater.from(requireContext()))

    private var startCallback: StartCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is StartCallback) startCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        startCallback = null
    }

    override fun initialiseFragment() = with(binding) {
        setClickListeners()
        emailTied.addTextChangedListener(setupTextWatcher(emailTil))
        passwordTied.addTextChangedListener(setupTextWatcher(passwordTil))
        repeatPasswordTied.addTextChangedListener(setupTextWatcher(repeatPasswordTil))
        observeValidationError()
        Unit
    }

    private fun setClickListeners() = with(binding) {
        registerB.setOnClickListener { onRegisterClick() }
        repeatPasswordTied.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                onRegisterClick()
                return@setOnEditorActionListener true
            }
            false
        }
        demoModeTv.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    private fun onRegisterClick() = with(binding) {
        invalidateErrors()
        viewModel.registerUser(
            emailTied.text.toString(),
            passwordTied.text.toString(),
            repeatPasswordTied.text.toString()
        )
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

    private fun invalidateErrors() = with(binding) {
        passwordTil.error = null
        passwordTil.isErrorEnabled = false
        emailTil.error = null
        emailTil.isErrorEnabled = false
        repeatPasswordTil.error = null
        repeatPasswordTil.isErrorEnabled = false
    }

    private fun observeValidationError() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.validationErrorFlow.collectLatest {
                val isEmailValid = it.isEmailValid && !it.isEmailEmpty
                val isPasswordValid = it.isPasswordValid && !it.isPasswordEmpty
                if (!isEmailValid) {
                    val error = when {
                        it.isEmailEmpty -> getString(R.string.email_empty_error)
                        else -> getString(R.string.email_validation_error)
                    }
                    setEmailError(error)
                }
                if (!isPasswordValid) {
                    passwordTil.error = when {
                        it.isPasswordEmpty -> getString(R.string.password_empty_error)
                        else -> getString(R.string.password_validation_error)
                    }
                    if (isEmailValid) {
                        passwordTil.requestFocus()
                        openKeyboard(passwordTied)
                    }
                }
                if (!it.isPasswordMatch) {
                    repeatPasswordTil.error = getString(R.string.password_do_not_match_error)
                    if (isEmailValid && isPasswordValid) {
                        repeatPasswordTil.requestFocus()
                        openKeyboard(repeatPasswordTied)
                    }
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
                            USER_ALREADY_EXIST_ERROR -> {
                                setEmailError(localizedMessaged)
                            }
                            else -> {
                                showErrorDialog(getString(R.string.all_error_something_wrong_title), localizedMessaged)
                                hideKeyboard()
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
                        hideKeyboard(requireView())
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
        val TAG = RegistrationFragment::class.java.simpleName
        fun newInstance() = RegistrationFragment()
    }
}