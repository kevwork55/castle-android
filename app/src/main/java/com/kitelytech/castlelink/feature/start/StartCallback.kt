package com.kitelytech.castlelink.feature.start

interface StartCallback {
    fun setLoadingState(isLoading: Boolean)
    fun startHomeScreen()
}