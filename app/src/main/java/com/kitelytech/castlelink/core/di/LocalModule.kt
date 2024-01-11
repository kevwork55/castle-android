package com.kitelytech.castlelink.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kitelytech.castlelink.data.local.api.scope.product.ProductData
import com.kitelytech.castlelink.data.local.impl.scope.product.ProductDataImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Provides
    @Singleton
    fun provideProductData(@ApplicationContext context: Context): ProductData {
        return ProductDataImpl(context)
    }

}