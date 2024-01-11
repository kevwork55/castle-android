package com.kitelytech.castlelink.feature.start.auth.registration

import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.util.Constant.emailRegex
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.feature.start.auth.state.RegistrationValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationRepository: AuthRepository
) : BaseViewModel() {
    private val _validationErrorFlow: MutableSharedFlow<RegistrationValidationError> =
        MutableSharedFlow()
    val validationErrorFlow: Flow<RegistrationValidationError> get() = _validationErrorFlow

    private val _requestStateFlow: MutableStateFlow<RequestState> =
        MutableStateFlow(RequestState.Idle)
    val requestStateFlow: Flow<RequestState> get() = _requestStateFlow

    fun registerUser(email: String, password: String, repeatPassword: String) {
        launchRequest {
            val isCredentialsValid = validateCredentials(email, password, repeatPassword)
            if (isCredentialsValid) {
                _requestStateFlow.emit(RequestState.Processing)
                val result = registrationRepository.registerUser(email, password)
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

    private suspend fun validateCredentials(
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        val isEmailEmpty = email.isBlank()
        val isEmailValid = emailRegex.matcher(email.trim()).find()
        val isPasswordEmpty = password.isBlank()
        val isLengthLongEnough = password.length >= 6
        val isPasswordMatch = password == repeatPassword

        val isCredentialsValid =
            isLengthLongEnough && isEmailValid && isPasswordMatch && !isEmailEmpty && !isPasswordEmpty
        if (!isCredentialsValid) {
            _validationErrorFlow.emit(
                RegistrationValidationError(
                    isEmailEmpty = isEmailEmpty,
                    isEmailValid = isEmailValid,
                    isPasswordEmpty = isPasswordEmpty,
                    isPasswordValid = isLengthLongEnough,
                    isPasswordMatch = isPasswordMatch
                )
            )
        }
        return isCredentialsValid
    }
}