package com.kitelytech.castlelink.feature.start.splash

import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.data.repo.api.user.UserRepository
import com.kitelytech.castlelink.data.repo.impl.user.FirebaseUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _requestStateFlow: MutableStateFlow<RequestState> = MutableStateFlow(RequestState.Idle)
    val requestStateFlow: Flow<RequestState> get() = _requestStateFlow

    fun isUserSignedUp() {
        launchRequest {
            _requestStateFlow.emit(RequestState.Processing)
            delay(500)
            val result = userRepository.getCurrentUser()
            when(result) {
                is RepoResult.Error -> {
                    _requestStateFlow.emit(RequestState.Error(result.errorCode, result.message))
                }
                is RepoResult.Success -> {
                    val isUserPresent = result.data != null
                    _requestStateFlow.emit(RequestState.Success(isUserPresent))
                }
            }
        }
    }
}