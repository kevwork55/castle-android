package com.kitelytech.castlelink.data.local.api.model.product

data class ConnectHelp(
    val uid: String,
    val deviceName: String?,
    val ghostDevice: String?,
    val requireLinkDevice: Boolean,
    val mobileEnabled: Boolean,
    val versionList: List<String>,
) {
}