package com.kitelytech.castlelink.custom.models

import com.google.gson.annotations.SerializedName

data class ProductSetting(
    @SerializedName("DeviceName")  var deviceName: String,
    @SerializedName("Sections")  var sections: MutableList<ProductSettingSection>,
)
