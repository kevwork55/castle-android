package com.kitelytech.castlelink.feature.start.forgotpassword

import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.util.Constant
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.feature.start.forgotpassword.state.ForgotPasswordValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    private val _validationErrorFlow: MutableSharedFlow<ForgotPasswordValidationError> =
        MutableSharedFlow()
    val validationErrorFlow: Flow<ForgotPasswordValidationError> get() = _validationErrorFlow

    private val _requestStateFlow: MutableStateFlow<RequestState> = MutableStateFlow(RequestState.Idle)
    val requestStateFlow: Flow<RequestState> get() = _requestStateFlow

    fun restorePassword(email: String) {
        launchRequest {
            val isCredentialsValid = validateCredentials(email)
            if (isCredentialsValid) {
                _requestStateFlow.emit(RequestState.Processing)
                val result = authRepository.restorePassword(email)
                when(result) {
                    is RepoResult.Error -> {
                        _requestStateFlow.emit(RequestState.Error(result.errorCode, result.message))
                    }
                    is RepoResult.Success -> {
                        _requestStateFlow.emit(RequestState.Success(Unit))
                    }
                }
            }
        }
    }

    private suspend fun validateCredentials(email: String): Boolean {
        val isEmailEmpty = email.isBlank()
        val isEmailValid = Constant.emailRegex.matcher(email.trim()).find()

        if (isEmailEmpty || !isEmailValid) {
            _validationErrorFlow.emit(
                ForgotPasswordValidationError(
                    isEmailEmpty = isEmailEmpty,
                    isEmailValid = isEmailValid
                )
            )
        }
        return isEmailValid
    }
}