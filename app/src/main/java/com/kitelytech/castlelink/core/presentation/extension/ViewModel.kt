package com.kitelytech.castlelink.core.presentation.extension

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.kitelytech.castlelink.core.presentation.ui.base.activity.BaseActivity
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel

@MainThread
fun <VM : BaseViewModel> BaseActivity<*, VM>.baseViewModels(
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }

    return ViewModelLazy(viewModelKClass, { viewModelStore }, factoryPromise)
}

@MainThread
fun <VM : BaseViewModel> BaseFragment<*, VM>.baseViewModels(
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }

    return ViewModelLazy(viewModelKClass, viewModelStoreOwner, factoryPromise)
}