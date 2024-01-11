package com.kitelytech.castlelink.custom.models

data class SettingSubgroup(
    var displayIndex : Int = -1,
    var hidden : Boolean = false,
    var settings : MutableList<Setting> = mutableListOf(),
    var title : String = ""
) {
    constructor(settingSubGroupMap : Map<String, Any>, productData: ProductData) : this() {
        for ((key, value) in settingSubGroupMap) {
            when(key.lowercase()) {
                "idx" -> {
                    displayIndex = value.toString().toInt()
                }
                "ttl" -> {
                    val stringIndex = value.toString().toInt()
                    title = productData.multiLang!![stringIndex].toString()
                }
                "hid" -> {
                    hidden = value.toString().toInt() != 0
                }
                "sets" -> {
                    val settingsMap : Map<String, Any> = value as Map<String, Any>
                    for ((k, v) in settingsMap) {
                        val settingMap : Map<String, Any> = v as Map<String, Any>
                        val setting = Setting(k, settingMap, productData)
                        settings.add(setting)
                    }
                }
            }
        }
    }

    fun displaySettings() : MutableList<Setting> {
        // add visible settings
        val displaySettings : MutableList<Setting> = mutableListOf()
        for (setting in settings) {
            if (setting.displayType() != DisplayType.none) {
                displaySettings.add(setting)
            }
        }
        // sort the settings into display order
        displaySettings.sortBy { it.displayIndex }
        return displaySettings
    }
}
