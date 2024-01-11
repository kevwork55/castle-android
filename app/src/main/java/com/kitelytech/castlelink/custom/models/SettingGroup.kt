package com.kitelytech.castlelink.custom.models

data class SettingGroup (
    var defaults : String = "",
    var subGroups : MutableList<SettingSubgroup> = mutableListOf()
) {
    constructor(settingGroupMap : Map<String, Any>, productData: ProductData) : this() {
        for ((key, value) in settingGroupMap) {
            val k = key.lowercase()
            if (k == "defaults") {
                // Save the defaults
                defaults = value.toString()
            } else if (k.take(2) == "sg") {
                // create the setting subgroup
                val settingSubGroupMap : Map<String, Any> = value as Map<String, Any>
                subGroups.add(SettingSubgroup(settingSubGroupMap, productData))
            }
        }
    }

    fun createDefaultValueSet() : SettingValueSet {
        val valueSet = createValueSet()

        // set to default values
        val memory = SettingMemory().initMemory(defaults)
        valueSet.setValues(memory)
        return valueSet
    }

    private fun createValueSet() : SettingValueSet {
        val valueSet = SettingValueSet()
        for (subgroup in subGroups) {
            for (setting in subgroup.settings) {
                valueSet.addSetting(setting)
            }
        }

        return valueSet
    }

    fun displaySubgroups() : MutableList<SettingSubgroup> {
        val displaySubgroups : MutableList<SettingSubgroup> = mutableListOf()
        for (subgroup in subGroups) {
            if (!subgroup.hidden && subgroup.displaySettings().isNotEmpty()) {
                displaySubgroups.add(subgroup)
            }
        }
        return displaySubgroups
    }

    fun dumpMemoryMap() : String {
        // create an array of all the settings
        val settings : MutableList<Setting> = mutableListOf()
        for (subgroup in subGroups) {
            for (setting in subgroup.settings) {
                if (setting.dataSize > 0 && setting.memoryArea == 0) {
                    settings.add(setting)
                }
            }
        }
        // sort the array by address
        settings.sortBy { it.address }
        settings.reverse()

        // dump the memory map
        var currentAddress = 0
        var string = ""

        while (settings.isNotEmpty()) {
            // get the last setting
            var setting : Setting
            if (settings.size > 0) {
                setting = settings[settings.size - 1]
                settings.removeAt(settings.size - 1)
            } else {
                continue
            }
            // mark unused memory
            if (currentAddress < setting.address) {
                val unusedSize = (setting.address - currentAddress)
                string += if (unusedSize > 1) {
                    "0x%04x".format(currentAddress) + ": [undocumented] (" + unusedSize + " bytes)\r\n"
                } else {
                    "0x%04x".format(currentAddress) + ": [undocumented] (" + unusedSize + " byte)\r\n"
                }
            }

            // mark setting memory
            string += if (setting.dataSize > 1) {
                "0x%04x".format(setting.address) + ": " + setting.title + " (" + setting.dataSize + " bytes)\r\n"
            } else {
                "0x%04x".format(setting.address) + ": " + setting.title + " (" + setting.dataSize + " byte)\r\n"
            }
            // advance
            currentAddress = setting.address + setting.dataSize
        }
        return string
    }
}