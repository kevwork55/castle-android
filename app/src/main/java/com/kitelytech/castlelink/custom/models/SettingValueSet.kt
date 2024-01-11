package com.kitelytech.castlelink.custom.models

class SettingValueSet {

    var settings : MutableList<Setting> = mutableListOf()
    var values : HashMap<String, SettingValue> = hashMapOf()

    fun addSetting(setting: Setting) {
        // safety check
        if (setting.key.isEmpty()) {
            return
        }

        // create the setting value
        val settingValue = SettingValue().initSetting(setting)
        settings.add(setting)
        if (settingValue != null) {
            values[setting.key] = settingValue
        }
    }

    fun settingMemory() : SettingMemory? {
        // calculate the required memory size for the settings
        var memorySize = 0
        for (setting in settings) {
            // TODO: implement these setting types
            if ((setting.type == SettingType.loggingDataEnabledV2) || setting.type == SettingType.dynamicCurve1024) {
                continue
            }

            if ((setting.dataSize > 0) && (setting.memoryArea == 0)) {
                val settingEndAddress = (setting.address + setting.dataSize)
                if (settingEndAddress > memorySize) {
                    memorySize = settingEndAddress
                }
            }
        }

        // create the memory
        var memory: SettingMemory? = null
        if (memorySize > 0) {
            memory = SettingMemory().initSize(memorySize)

            // write the settings to setting memory
            for ((_, value) in values) {
                value.write(memory)
            }
        }

        return memory
    }

    fun setValues(memory: SettingMemory?) {
        // read the values from setting memory
        for ((_, value) in values) {
            memory?.let { value.read(it) }
        }
    }

    fun dump() : String {
        val sortedSettings = settings.sortedBy { it.address }
        var string = ""
        for (setting in sortedSettings) {
            val settingValue = values[setting.key]
            val intValue = settingValue?.getIntValue()
            if (setting.title.isNotEmpty()) {
                string += "'" + setting.title + "': " + intValue + " (" + "0x%x".format(intValue) + ")\r\n"
            }
        }
        return string
    }
}

