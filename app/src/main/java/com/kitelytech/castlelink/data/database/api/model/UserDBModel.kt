package com.kitelytech.castlelink.data.database.api.model

data class UserDBModel(
    val uid: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val location: String,
    val language: String,
    val profilePicture: String
)
