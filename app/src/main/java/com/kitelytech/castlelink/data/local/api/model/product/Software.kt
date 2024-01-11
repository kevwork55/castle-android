package com.kitelytech.castlelink.data.local.api.model.product

class Software(
    val uid: String,
    val versionId: String?,
    val custVer: String?,
    val settingGroupKey: Int?,
    val isBeta: Boolean,
    val isActive: Boolean,
    val isProduction: Boolean,
    val isBroken: Boolean,
    val relDate: String?,
    val revText: Int?,
    val milestone: Boolean,
    val resetLink: Boolean,
) {
}