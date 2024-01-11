package com.kitelytech.castlelink.core.presentation.util

import java.util.regex.Pattern

object Constant {
    const val EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
    val emailRegex = Pattern.compile(Constant.EMAIL_REGEX, Pattern.CASE_INSENSITIVE)

    const val START_FROM_LOGIN_SCREEN_EXTRA_KEY = "START_FROM_LOGIN_SCREEN_EXTRA_KEY"
}