package com.kitelytech.castlelink.data.repo.api.auth

import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.data.repo.api.model.UserRepoModel

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): RepoResult<Unit>
    suspend fun loginUser(email: String, password: String): RepoResult<Unit>
    suspend fun logOut(): RepoResult<Unit>
    suspend fun restorePassword(email: String): RepoResult<Unit>
}