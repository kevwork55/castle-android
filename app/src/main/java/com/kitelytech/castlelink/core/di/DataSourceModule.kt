package com.kitelytech.castlelink.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kitelytech.castlelink.core.di.Constant.ACCOUNTS_QUALIFIER
import com.kitelytech.castlelink.data.database.api.scope.user.UserDBSource
import com.kitelytech.castlelink.data.database.impl.scope.auth.FirebaseUserDBSource
import com.kitelytech.castlelink.data.network.api.scope.auth.AuthNetSource
import com.kitelytech.castlelink.data.network.impl.scope.auth.FirebaseRegistrationNetSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRegistrationNetSource(firebaseAuth: FirebaseAuth): AuthNetSource {
        return FirebaseRegistrationNetSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideRegistrationDBSource(@Named(ACCOUNTS_QUALIFIER) accountDatabaseRef: DatabaseReference, firebaseAuth: FirebaseAuth): UserDBSource {
        return FirebaseUserDBSource(accountDatabaseRef, firebaseAuth)
    }
}