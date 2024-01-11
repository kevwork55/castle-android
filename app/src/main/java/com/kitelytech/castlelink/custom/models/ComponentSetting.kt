package com.kitelytech.castlelink.custom.models

import com.google.gson.annotations.SerializedName

data class ComponentSetting(
    @SerializedName("name")  var name: String,
    @SerializedName("type")  var type: String,
    @SerializedName("min_value")  var minValue: String?,
    @SerializedName("max_value")  var maxValue: String?,
    @SerializedName("value_type")  var valueType: String,
    @SerializedName("options")  var options: List<String>,
    @SerializedName("Measurement")  var measurement: String,
    var value: String?,
)
