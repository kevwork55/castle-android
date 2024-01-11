package com.kitelytech.castlelink.custom.models

data class ConnectHelp(
    var demoFirmwareVersion : String = "",
    var deviceName : String = "",
    var firmwareVersions : String = "",
    var mobileEnabled : Boolean = false,
    var product: Product? = null,
    var settingGroup: SettingGroup? = null
) {
    constructor(connectHelpMap : Map<String, Any>, productData: ProductData) : this() {
        for ((key, value) in connectHelpMap) {
            when (key.lowercase()) {
                "device_name" -> {
                    // device display name (string)
                    this.deviceName = value.toString()
                }
                "ghost_device" -> {
                    // firmware version for demo mode (string)
                    this.demoFirmwareVersion = value.toString()
                }
                "mobile_enabled" -> {
                    // enable on mobile app flag (integer)
                    this.mobileEnabled = (value.toString().toInt() != 0)
                }
                "requires_link_device" -> {
                    // requires link device (integer)
                    // Note: This is ignored.
                }
                "version_list" -> {
                    // firmware version list (string)
                    this.firmwareVersions = value.toString()
                }
            }
        }
    }
}