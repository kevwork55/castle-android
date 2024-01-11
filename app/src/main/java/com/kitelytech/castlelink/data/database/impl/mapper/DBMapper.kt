package com.kitelytech.castlelink.data.database.impl.mapper

import com.google.firebase.auth.FirebaseUser
import com.kitelytech.castlelink.data.database.api.model.UserDBModel

fun FirebaseUser.toDB(): UserDBModel {
    val email = this.email.orEmpty()
    val username = if (email.contains('@')) {
        email.substring(0, email.indexOf('@'))
    } else {
        ""
    }
    return UserDBModel(
        uid = this.uid,
        username = username,
        firstName = "",
        lastName = "",
        location = "",
        language = "",
        profilePicture = "",
    )
}

fun UserDBModel.toFirebaseDataMap(): Map<String, String> {
    return mapOf(
        "name_first" to firstName,
        "name_last" to lastName,
        "username" to username,
        "location" to location,
        "language" to language,
        "profilePicture" to profilePicture,
    )
}