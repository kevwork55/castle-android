package com.kitelytech.castlelink.feature.start.forgotpassword.state

data class ForgotPasswordValidationError(
    val isEmailEmpty: Boolean,
    val isEmailValid: Boolean
)
