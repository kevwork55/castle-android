package com.kitelytech.castlelink.core.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kitelytech.castlelink.core.di.Constant.ACCOUNTS_PATH
import com.kitelytech.castlelink.core.di.Constant.ACCOUNTS_QUALIFIER
import com.kitelytech.castlelink.core.di.Constant.USER_DATA_PATH
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealtimeDataBase(): FirebaseDatabase {
        return Firebase.database
    }

    @Singleton
    @Provides
    @Named(value = ACCOUNTS_QUALIFIER)
    fun provideAccountRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child(USER_DATA_PATH).child(ACCOUNTS_PATH)
    }
}