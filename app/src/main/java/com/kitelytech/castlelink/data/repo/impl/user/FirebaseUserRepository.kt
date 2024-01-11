package com.kitelytech.castlelink.data.repo.impl.user

import com.kitelytech.castlelink.data.database.api.scope.user.UserDBSource
import com.kitelytech.castlelink.data.repo.api.BaseRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import com.kitelytech.castlelink.data.repo.api.model.UserRepoModel
import com.kitelytech.castlelink.data.repo.api.user.UserRepository
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val userDBSource: UserDBSource
) : BaseRepository(), UserRepository {

    override suspend fun getCurrentUser(): RepoResult<UserRepoModel?> {
        return launch {
            userDBSource.getCurrentUser()?.let {
                UserRepoModel(it.uid)
            }
        }
    }

}