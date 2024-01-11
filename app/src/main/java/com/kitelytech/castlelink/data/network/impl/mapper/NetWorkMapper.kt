package com.kitelytech.castlelink.data.network.impl.mapper

import com.google.firebase.auth.FirebaseUser
import com.kitelytech.castlelink.data.network.api.model.UserNetModel

fun FirebaseUser.toNet(): UserNetModel {
    val email = this.email.orEmpty()
    val username = if (email.contains('@')) {
        email.substring(0, email.indexOf('@'))
    } else {
        ""
    }
    return UserNetModel(
        uid = this.uid,
        username = username,
        email = email
    )
}