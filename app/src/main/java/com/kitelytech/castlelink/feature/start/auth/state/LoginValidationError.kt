package com.kitelytech.castlelink.feature.start.auth.state

data class LoginValidationError(
    val isEmailEmpty: Boolean,
    val isEmailValid: Boolean,
    val isPasswordEmpty: Boolean
)
