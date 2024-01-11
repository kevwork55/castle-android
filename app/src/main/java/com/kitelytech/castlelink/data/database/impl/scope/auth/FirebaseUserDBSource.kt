package com.kitelytech.castlelink.data.database.impl.scope.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kitelytech.castlelink.data.database.api.scope.user.UserDBSource
import com.kitelytech.castlelink.data.database.api.DBSource
import com.kitelytech.castlelink.data.database.api.model.UserDBModel
import com.kitelytech.castlelink.data.database.impl.mapper.toDB
import com.kitelytech.castlelink.data.database.impl.mapper.toFirebaseDataMap
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDBSource @Inject constructor(
    private val accountDatabaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : DBSource(), UserDBSource {

    override suspend fun updateUser(user: UserDBModel) {
        val userRef = accountDatabaseReference.child(user.uid)
        userRef.updateChildren(user.toFirebaseDataMap()).await()
    }

    override suspend fun getCurrentUser(): UserDBModel? {
        return firebaseAuth.currentUser?.toDB()
    }
}
