package com.kitelytech.castlelink.data.repo.api.user

import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.data.repo.api.model.UserRepoModel

interface UserRepository {
    suspend fun getCurrentUser(): RepoResult<UserRepoModel?>
}