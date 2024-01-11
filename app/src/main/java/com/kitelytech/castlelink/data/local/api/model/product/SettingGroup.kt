package com.kitelytech.castlelink.data.local.api.model.product

class SettingGroup(
    val uid: String,
    val defaults: String?,
    val subSettingsGroup: List<SubSettingGroup>
) {
}