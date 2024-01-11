package com.kitelytech.castlelink.custom.models

data class SettingOption(
    var disable : Boolean = false,
    var disablesSettings : MutableList<String> = mutableListOf(),
    var displayIndex : Int = -1,
    var help : String = "",
    var isDefault : Boolean = false,
    var numericTitle : Double = 0.0,
    var title: String = "",
    var value : Int = -1
) {
    constructor(optionMap: Map<String, Any>, productData: ProductData) : this() {

    }
}