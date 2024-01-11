package com.kitelytech.castlelink.feature.home

import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.data.local.api.scope.product.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productData: ProductData
): BaseViewModel() {
    fun parse() {
        productData.parseProductData()
    }
}