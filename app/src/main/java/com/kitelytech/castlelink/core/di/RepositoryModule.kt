package com.kitelytech.castlelink.core.di

import com.kitelytech.castlelink.data.database.api.scope.user.UserDBSource
import com.kitelytech.castlelink.data.network.api.scope.auth.AuthNetSource
import com.kitelytech.castlelink.data.repo.api.auth.AuthRepository
import com.kitelytech.castlelink.data.repo.api.user.UserRepository
import com.kitelytech.castlelink.data.repo.impl.auth.FirebaseAuthRepository
import com.kitelytech.castlelink.data.repo.impl.user.FirebaseUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        registrationNetSource: AuthNetSource,
        userDBSource: UserDBSource
    ): AuthRepository {
        return FirebaseAuthRepository(registrationNetSource, userDBSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDBSource: UserDBSource): UserRepository {
        return FirebaseUserRepository(userDBSource)
    }
}