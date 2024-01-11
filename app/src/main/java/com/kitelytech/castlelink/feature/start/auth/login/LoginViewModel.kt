package com.kitelytech.castlelink.feature.start.auth.login

import com.kitelytech.castlelink.core.common.ErrorCode.INVALID_CREDENTIALS_ERROR
import com.kitelytech.castlelink.core.common.ErrorCode.USER_DO_NOT_EXIST_ERROR
import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.util.Constant.emailRegex
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.feature.start.auth.state.LoginValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    private val _validationErrorFlow: MutableSharedFlow<LoginValidationError> = MutableSharedFlow()
    val validationErrorFlow: Flow<LoginValidationError> get() = _validationErrorFlow

    private val _requestStateFlow: MutableStateFlow<RequestState> =
        MutableStateFlow(RequestState.Idle)
    val requestStateFlow: Flow<RequestState> get() = _requestStateFlow

    fun loginUser(email: String, password: String) {
        launchRequest {
            val isCredentialsValid = validateCredentials(email, password)
            if (isCredentialsValid) {
                _requestStateFlow.emit(RequestState.Processing)
                val result = authRepository.loginUser(email, password)
                when (result) {
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

    private suspend fun validateCredentials(email: String, password: String): Boolean {
        val isEmailEmpty = email.isBlank()
        val isEmailValid = emailRegex.matcher(email.trim()).find()
        val isPasswordEmpty = password.isBlank()

        val isCredentialsValid = !isEmailEmpty && isEmailValid && !isPasswordEmpty
        if (!isCredentialsValid) {
            _validationErrorFlow.emit(
                LoginValidationError(
                    isEmailEmpty = isEmailEmpty,
                    isEmailValid = isEmailValid,
                    isPasswordEmpty = isPasswordEmpty
                )
            )
        }
        return isCredentialsValid
    }
}