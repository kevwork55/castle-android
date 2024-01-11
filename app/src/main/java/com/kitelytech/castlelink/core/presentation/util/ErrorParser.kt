package com.kitelytech.castlelink.core.presentation.util

import android.content.Context
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.common.ErrorCode.DON_NOT_HAVE_PERMISSION_FOR_DATABASE_ERROR
import com.kitelytech.castlelink.core.common.ErrorCode.INVALID_CREDENTIALS_ERROR
import com.kitelytech.castlelink.core.common.ErrorCode.NO_INTERNET_CONNECTION_ERROR
import com.kitelytech.castlelink.core.common.ErrorCode.USER_ALREADY_EXIST_ERROR
import com.kitelytech.castlelink.core.common.ErrorCode.USER_DO_NOT_EXIST_ERROR

object ErrorParser {
    fun parseError( context: Context, errorCode: Int, defaultMessage: String?): String {
        return when (errorCode) {
            USER_ALREADY_EXIST_ERROR -> context.getString(R.string.registration_email_already_exist)
            USER_DO_NOT_EXIST_ERROR -> context.getString(R.string.login_email_not_exist)
            NO_INTERNET_CONNECTION_ERROR -> context.getString(R.string.all_no_internet_connection_error)
            INVALID_CREDENTIALS_ERROR -> context.getString(R.string.login_invalid_credentials)
            DON_NOT_HAVE_PERMISSION_FOR_DATABASE_ERROR -> context.getString(R.string.database_permission_error)
            else -> defaultMessage ?: context.getString(R.string.all_unknown_error)
        }
    }
}