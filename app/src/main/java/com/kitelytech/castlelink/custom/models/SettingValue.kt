package com.kitelytech.castlelink.custom.models

class SettingValue {

    var setting: Setting = Setting()
    var data: MutableList<UInt> = mutableListOf()
    var intValue : Int = 0

    @JvmName("getIntValue1")
    fun getIntValue() : Int {
        var currentValue = 0
        for (i in 0 until data.count()) {
            currentValue = (currentValue shl 8)
            currentValue = (currentValue or data[i].toInt())
        }
        return currentValue
    }

    @JvmName("setIntValue1")
    fun setIntValue(intValue: Int) {
        var currentValue = intValue
        for (i in 0 until data.count()) {
            data[i] = ((currentValue and 0xff).toUInt())
            currentValue = (currentValue shr 8)
        }
        this.intValue = currentValue
    }

    fun initSetting(setting: Setting) : SettingValue? {
        // safety check
        if (setting.address >= 0 && setting.dataType != DataType.None) {
            if (setting.dataSize > 0) {
                this.setting = setting
                this.data = mutableListOf()
                for (i in 0 until setting.dataSize) {
                    this.data.add(0u)
                }

                // set the data to the default option
                var foundDefault = false
                val options = setting.options
                if (options?.isNotEmpty() == true) {
                    for (option in options) {
                        if (option.isDefault) {
                            setIntValue(option.value)
                            foundDefault = true
                            break
                        }
                    }

                    if (!foundDefault) {
                        // use the first option
                        setIntValue(options[0].value)
                        foundDefault = true
                    }
                }

                // use the minmum for linear spinners
                if (!foundDefault && setting.settingLinearSpinner != null) {
                    val minimum = setting.settingLinearSpinner!!.minimum
                    setIntValue(setting.settingLinearSpinner!!.rawValue(minimum))
                    foundDefault = true
                }

                return SettingValue()
            } else {
                return null
            }
        } else {
            return null
        }
    }

    fun read(memory: SettingMemory) {
        // safety checks
        if ((setting.address + setting.dataSize) >= memory.bytes.count()) {
            return
        }

        if (setting.memoryArea != 0) {
            return
        }

        // safety checks
        for (count in 0 until setting.dataSize) {
            data[count] = memory.bytes[setting.address + count]
        }
    }

    fun write(memory: SettingMemory) {
        // safety checks
        if (setting.dataSize > 0 && setting.memoryArea == 0 && (setting.address + setting.dataSize) <= memory.bytes.count()) {
            // copy the data
            for (count in 0 until setting.dataSize) {
                memory.bytes[setting.address + count] = data[count]
            }
        } else {
            return
        }
    }
}