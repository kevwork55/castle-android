package com.kitelytech.castlelink.data.repo.impl.auth

import com.kitelytech.castlelink.data.database.api.scope.user.UserDBSource
import com.kitelytech.castlelink.data.database.api.model.UserDBModel
import com.kitelytech.castlelink.data.network.api.scope.auth.AuthNetSource
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.BaseRepository
import com.kitelytech.castlelink.data.repo.api.model.RepoResult
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val authNetSource: AuthNetSource,
    private val userDBSource: UserDBSource,
) : BaseRepository(), AuthRepository {

    override suspend fun registerUser(email: String, password: String): RepoResult<Unit> {
        return launch {
            val it = authNetSource.registerUser(email, password)
            it?.let {
                userDBSource.updateUser(
                    UserDBModel(
                        it.uid,
                        it.username,
                        "", "", "", "", ""
                    )
                )
            }
        }
    }

    override suspend fun loginUser(email: String, password: String): RepoResult<Unit> {
        return launch {
            authNetSource.loginUser(email, password)
        }
    }

    override suspend fun logOut(): RepoResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun restorePassword(email: String): RepoResult<Unit> {
        return launch {
            authNetSource.restorePassword(email)
        }
    }

}