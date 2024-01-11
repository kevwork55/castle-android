package com.kitelytech.castlelink.core.presentation.model

sealed class RequestState {
    object Idle: RequestState()
    object Processing: RequestState()
    data class Success(val data: Any?): RequestState()
    data class Error(val errorCode: Int, val message: String?): RequestState()
}
