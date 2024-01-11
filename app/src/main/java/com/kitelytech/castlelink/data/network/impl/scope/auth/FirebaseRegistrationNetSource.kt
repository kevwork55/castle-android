package com.kitelytech.castlelink.data.network.impl.scope.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.kitelytech.castlelink.data.network.api.scope.auth.AuthNetSource
import com.kitelytech.castlelink.data.network.api.NetworkSource
import com.kitelytech.castlelink.data.network.api.model.UserNetModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRegistrationNetSource @Inject constructor(
    private val auth: FirebaseAuth
) : NetworkSource(), AuthNetSource {

    override suspend fun registerUser(email: String, password: String): UserNetModel? {
        val authResult: AuthResult = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user
        return firebaseUser?.let {
            UserNetModel(
                firebaseUser.uid,
                firebaseUser.email.orEmpty(),
                if (email.contains('@')) {
                    email.substring(0, email.indexOf('@'))
                } else ""
            )
        }

    }

    override suspend fun loginUser(email: String, password: String) {
        val authResult: AuthResult = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user
        firebaseUser?.let {
            UserNetModel(
                firebaseUser.uid,
                firebaseUser.email.orEmpty(),
                if (email.contains('@')) {
                    email.substring(0, email.indexOf('@'))
                } else ""
            )
        }
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun restorePassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

}