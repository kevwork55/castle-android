package com.kitelytech.castlelink.custom.models

import com.google.gson.annotations.SerializedName

data class ProductSettingSection(
    @SerializedName("title")  var title: String,
    @SerializedName("hidden")  var hidden: Boolean,
    @SerializedName("settings")  var settings: List<ComponentSetting>,
)
