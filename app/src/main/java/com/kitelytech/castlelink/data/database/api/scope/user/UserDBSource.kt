package com.kitelytech.castlelink.data.database.api.scope.user

import com.kitelytech.castlelink.data.database.api.model.UserDBModel

interface UserDBSource {
    suspend fun updateUser(user: UserDBModel)
    suspend fun getCurrentUser(): UserDBModel?
}