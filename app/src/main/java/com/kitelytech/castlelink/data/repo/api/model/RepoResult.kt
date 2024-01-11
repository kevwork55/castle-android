package com.kitelytech.castlelink.data.repo.api.model

sealed class RepoResult<T> {
    data class Success<T>(val data: T? = null): RepoResult<T>()
    data class Error<T>(val errorCode: Int, val message: String?): RepoResult<T>()
}
