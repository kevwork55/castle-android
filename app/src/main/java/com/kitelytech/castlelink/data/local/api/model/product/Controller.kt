package com.kitelytech.castlelink.data.local.api.model.product

class Controller(
    val uid: String,
    val name: String?,
    val productClass: String?,
    val isMobileEnabled: Boolean,
    val minVolt: Double?,
    val maxVolt: Double?,
    val softwareList: List<Software>
) {
}