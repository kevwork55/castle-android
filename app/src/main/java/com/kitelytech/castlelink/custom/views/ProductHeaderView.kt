package com.kitelytech.castlelink.custom.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.Product
import kotlinx.android.synthetic.main.layout_product_header.view.*

@SuppressLint("ViewConstructor")
class ProductHeaderView constructor(
    context: Context,
    product: Product
) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_product_header, this, true)
        tvProductName.text = product.name
    }
}