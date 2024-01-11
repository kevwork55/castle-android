package com.kitelytech.castlelink.custom.models

data class Firmware(
    var active : Boolean = true,
    var beta : Boolean = false,
    var broken : Boolean = false,
    var displayVersion: String = "",
    var production : Boolean = false,
    var releaseDate: String = "",
    var resetLink : Boolean = false,
    var revisionNotes : String = "",
    var settingGroup : SettingGroup? = null,
    var versionID : String = ""
) {
    constructor(firmwareMap : Map<String, Any>, productData: ProductData) : this() {
        for ((key, value) in firmwareMap) {
            when (key.lowercase()) {
                "verid" -> {
                    // firmware version id (string)
                    this.versionID = value.toString()
                }
                "custver" -> {
                    // display string for firmware version (string)
                    this.displayVersion = value.toString()
                }
                "setgrpkey" -> {
                    // setting group key (integer)
                    val settingGroupKey = value.toString().toInt()
                    this.settingGroup = productData.settingGroups[settingGroupKey]
                }
                "active" -> {
                    // active flag (integer)
                    this.active = (value.toString().toInt() != 0)
                }
                "beta" -> {
                    // beta flag (integer)
                    this.beta = (value.toString().toInt() != 0)
                }
                "broken" -> {
                    // broken flag (integer)
                    this.broken = (value.toString().toInt() != 0)
                }
                "milestone" -> {
                    // milestone (integer)
                    // Note: This is ignored.
                }
                "production" -> {
                    // product flag (integer)
                    this.production = (value.toString().toInt() != 0)
                }
                "reldate" -> {
                    // release date (string)
                    this.releaseDate = value.toString()
                }
                "resetlink" -> {
                    // reset link flag (integer)
                    this.resetLink = (value.toString().toInt() != 0)
                }
                "revtext" -> {
                    // revision text string index (integer)
                    val stringIndex = value.toString().toInt()
                    this.revisionNotes = productData.multiLang!![stringIndex].toString()
                }
            }
        }
    }
}