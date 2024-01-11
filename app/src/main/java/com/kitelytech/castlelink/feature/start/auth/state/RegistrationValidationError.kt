package com.kitelytech.castlelink.feature.start.auth.state

data class RegistrationValidationError(
    val isEmailEmpty: Boolean,
    val isEmailValid: Boolean,
    val isPasswordEmpty: Boolean,
    val isPasswordValid: Boolean,
    val isPasswordMatch: Boolean
)
