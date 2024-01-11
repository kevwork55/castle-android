package com.kitelytech.castlelink.data.repo.api

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseException
import com.kitelytech.castlelink.core.common.ErrorCode
import com.kitelytech.castlelink.core.common.ErrorCode.UNKNOWN_ERROR
import com.kitelytech.castlelink.data.repo.api.model.RepoResult

abstract class BaseRepository {
    protected suspend inline fun <T> launch(
        crossinline block: suspend () -> T
    ): RepoResult<T> {
        return try {
            RepoResult.Success(block())
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is FirebaseAuthUserCollisionException -> {
                    RepoResult.Error(ErrorCode.USER_ALREADY_EXIST_ERROR, e.message)
                }
                is FirebaseAuthInvalidUserException -> {
                    RepoResult.Error(ErrorCode.USER_DO_NOT_EXIST_ERROR, e.message)
                }
                is FirebaseNetworkException -> {
                    RepoResult.Error(ErrorCode.NO_INTERNET_CONNECTION_ERROR, e.message)
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    RepoResult.Error(ErrorCode.INVALID_CREDENTIALS_ERROR, e.message)
                }
                is DatabaseException -> {
                    RepoResult.Error(ErrorCode.DON_NOT_HAVE_PERMISSION_FOR_DATABASE_ERROR, e.message)
                }
                else -> {
                    RepoResult.Error(UNKNOWN_ERROR, e.message)
                }
            }
        }
    }
}