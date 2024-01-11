package com.kitelytech.castlelink.data.network.api.scope.auth

import com.kitelytech.castlelink.data.network.api.model.UserNetModel

interface AuthNetSource {
    suspend fun registerUser(email: String, password: String): UserNetModel?
    suspend fun loginUser(email: String, password: String)
    suspend fun logOut()
    suspend fun restorePassword(email: String)
}