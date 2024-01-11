package com.kitelytech.castlelink.custom.models

data class ProductData (
    var databaseInfo: DatabaseInfo? = null,
    var multiLang: Map<Int, String>? = null,
    var settingGroups: Map<Int, SettingGroup> = mapOf(),
    var products: Map<String, Product> = mapOf(),
    var firmwareLookup : Map<String, Map<String, Any>> = mapOf(),
    var connectHelps: MutableList<ConnectHelp> = mutableListOf(),
    var connectionHelpSections: MutableList<ConnectHelpSection> = mutableListOf(),
    var sections: MutableList<Section> = mutableListOf()
) {
    constructor(databaseInfo: DatabaseInfo, multiLang: Map<Int, String>) : this() {
        this.databaseInfo = databaseInfo
        this.multiLang = multiLang
    }
}