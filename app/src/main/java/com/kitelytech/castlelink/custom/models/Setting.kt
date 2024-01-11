package com.kitelytech.castlelink.custom.models

import android.util.Log

data class Setting(
    var address: Int = -1,
    var dataSize: Int = -1,
    var dataType: DataType = DataType.None,
    var displayIndex: Int = -1,
    var help: String = "",
    var key: String = "",
    var memoryArea: Int = -1,
    var options: MutableList<SettingOption>? = mutableListOf(),
    var title: String = "",
    var type: SettingType = SettingType.none,
    var settingLinearSpinner: SettingLinearSpinner? = null
) {
    constructor(title : String) : this() {
        this.title = title
    }

    constructor(key: String, settingMap: Map<String, Any>, productData: ProductData) : this() {
        this.key = key
        options?.sortBy { displayIndex }

        val typeValue = settingMap["Type"].toString().toInt()
        val type = convertSettingType(typeValue)
        if (type == SettingType.linearSpinner) {
            this.settingLinearSpinner = SettingLinearSpinner(options, settingMap)
        } else {
            // parse the setting map
            this.settingLinearSpinner = null
            for ((settingKey, settingValue ) in settingMap) {
                if (!decodeKey(settingKey.lowercase(), settingValue, productData)) {
                    Log.e("Key", "Found unhandled key")
                }
            }
        }
    }

    private fun decodeKey(key: String, value: Any, productData: ProductData) : Boolean {
        when (key) {
            "type" -> {
                // setting type enum (integer)
                val typeValue = value.toString().toInt()
                this.type = convertSettingType(typeValue)
                return true
            }
            "idx" -> {
                // display index (integer)
                this.displayIndex = value.toString().toInt()
                return true
            }
            "ttl" -> {
                // title string index (integer)
                val stringIndex = value.toString().toInt()
                this.title = productData.multiLang!![stringIndex].toString()
                return true
            }
            "help" -> {
                // help string index (integer)
                val stringIndex = value.toString().toInt()
                this.help = productData.multiLang!![stringIndex].toString()
                return true
            }
            "add" -> {
                // controller memory address (integer)
                this.address = value.toString().toInt()
                return true
            }
            "dsz" -> {
                // data size in bytes (integer)
                this.dataSize = value.toString().toInt()
                return true
            }
            "dtyp" -> {
                // data type enum (integer)
                val dataTypeValue = value.toString().toInt()
                this.dataType = convertDataType(dataTypeValue)
                return true
            }
            "memarea" -> {
                // controller memory area (integer)
                this.memoryArea = value.toString().toInt()
                return true
            }
            "opts" -> {
                // options
                val optionsMap : Map<String, Any> = value as Map<String, Any>
                val options : MutableList<SettingOption> = mutableListOf()
                for ((_, optionValue) in optionsMap) {
                    val optionMap : Map<String, Any> = optionValue as Map<String, Any>
                    val option = SettingOption(optionMap, productData)
                    options.add(option)
                }

                if (options.isNotEmpty()) {
                    this.options = options
                }
                return true
            }
            "clock_freq" -> {
                return true
            }
            "datalog_freq" -> {
                return true
            }
            "divider" -> {
                return true
            }
            "arm_count_default" -> {
                return true
            }
            "arm_count_max" -> {
                return true
            }
            "arm_count_min" -> {
                return true
            }
            "max_count_default" -> {
                return true
            }
            "max_count_max" -> {
                return true
            }
            "max_count_min" -> {
                return true
            }
            "spooled_up" -> {
                return true
            }
            "spool_up" -> {
                return true
            }
            "governor_gain" -> {
                return true
            }
            "throttle_response" -> {
                return true
            }
            "warns" -> {
                return true
            }
            else -> return false
        }
    }

    private fun convertSettingType(typeValue: Int) : SettingType {
        return when (typeValue) {
            1 -> SettingType.requiredHidden
            2 -> SettingType.hidden
            3 -> SettingType.setValue
            100 -> SettingType.simpleLabel
            101 -> SettingType.simpleCheckBox
            102 -> SettingType.simpleComboBox
            103 -> SettingType.readOnlyCheckBox
            104 -> SettingType.simpleGroupBox
            105 -> SettingType.checkBitField
            500 -> SettingType.escSoftwareUpdate
            501 -> SettingType.voltageCutoff
            502 -> SettingType.dynamicCurve
            503 -> SettingType.linearSpinner
            504 -> SettingType.governorGain
            505 -> SettingType.bergFreq
            506 -> SettingType.bergControl
            507 -> SettingType.bergChannelSlider
            508 -> SettingType.sequentialMemory
            509 -> SettingType.thunderbirdVoltageCutoff
            510 -> SettingType.millisecondDisplayLabel
            511 -> SettingType.loggingDataOperations
            512 -> SettingType.advancedThrottle
            513 -> SettingType.fileOperations
            514 -> SettingType.thunderbirdVoltageCutoff2
            515 -> SettingType.loggingDataEnabled
            516 -> SettingType.cheatActivationRange
            517 -> SettingType.advancedThrottleLongFormat
            518 -> SettingType.torqueLimit
            519 -> SettingType.advancedThrottleLongFormatNewEndpoints
            520 -> SettingType.alarmPresets
            521 -> SettingType.alarmTemperature
            522 -> SettingType.rpmAndPoleCountParent
            523 -> SettingType.rpmAndPoleCountChild
            524 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorNoSimple
            525 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor
            526 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorExternalGov
            527 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor2ExternalGov
            528 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorNoSimpleExternalGov
            529 -> SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor2NoSimpleExternalGov
            530 -> SettingType.rx2Mode
            531 -> SettingType.multiRotorEndpoints
            532 -> SettingType.dynamicCurve1024
            533 -> SettingType.comboCheck
            534 -> SettingType.loggingDataEnabledV2
            1001 -> SettingType.dyneticSystem
            1002 -> SettingType.industrialThrottle
            1050 -> SettingType.riniControllerRPM
            1051 -> SettingType.resetLog
            1052 -> SettingType.onTimeLogOperations
            9999 -> SettingType.none
            else -> SettingType.none
        }
    }

    private fun convertDataType(typeValue: Int) : DataType {
        return when(typeValue) {
            -1 -> DataType.None
            0 -> DataType.Byte
            1 -> DataType.cInt
            2 -> DataType.cLong
            3 -> DataType.cFloat
            100 -> DataType.multiByte
            150 -> DataType.separateBytes
            else -> DataType.None
        }
    }

    fun displayType() : DisplayType {
        return when (type) {
            SettingType.advancedThrottle, SettingType.advancedThrottleLongFormat, SettingType.advancedThrottleLongFormatNewEndpoints,
                SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor, SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor2ExternalGov,
                SettingType.advancedThrottleLongFormatNewEndpointsMultiRotor2NoSimpleExternalGov, SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorExternalGov,
                SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorNoSimple, SettingType.advancedThrottleLongFormatNewEndpointsMultiRotorNoSimpleExternalGov -> {
                    DisplayType.advancedThrottleGroup
                }
            SettingType.cheatActivationRange -> DisplayType.cheatActivationRange
            SettingType.checkBitField -> DisplayType.checkBitField
            SettingType.comboCheck -> DisplayType.comboCheckbox
            SettingType.dynamicCurve, SettingType.dynamicCurve1024 -> DisplayType.dynamicCurve
            SettingType.linearSpinner -> DisplayType.linearSpinner
            SettingType.loggingDataEnabled, SettingType.loggingDataEnabledV2 -> DisplayType.dataLogEnabled
            SettingType.multiRotorEndpoints -> DisplayType.multiRotorEndpoints
            SettingType.readOnlyCheckBox -> DisplayType.readOnlyCheckbox
            SettingType.rx2Mode -> DisplayType.checkboxOverlay
            SettingType.simpleCheckBox -> DisplayType.simpleCheckbox
            SettingType.simpleComboBox -> DisplayType.simpleCombo
            SettingType.torqueLimit -> DisplayType.torqueLimit
            SettingType.voltageCutoff -> DisplayType.voltageCutoffGroup
            else -> DisplayType.none
        }
    }
}

enum class DataType {
    None, Byte, cInt, cLong, cFloat, multiByte, separateBytes
}

enum class SettingType {
    requiredHidden,
    hidden,
    setValue,
    simpleLabel,
    simpleCheckBox,
    simpleComboBox,
    readOnlyCheckBox,
    simpleGroupBox,
    checkBitField,
    escSoftwareUpdate,
    voltageCutoff,
    dynamicCurve,
    linearSpinner,
    governorGain,
    bergFreq,
    bergControl,
    bergChannelSlider,
    sequentialMemory,
    thunderbirdVoltageCutoff,
    millisecondDisplayLabel,
    loggingDataOperations,
    advancedThrottle,
    fileOperations,
    thunderbirdVoltageCutoff2,
    loggingDataEnabled,
    cheatActivationRange,
    advancedThrottleLongFormat,
    torqueLimit,
    advancedThrottleLongFormatNewEndpoints,
    alarmPresets,
    alarmTemperature,
    rpmAndPoleCountParent,
    rpmAndPoleCountChild,
    advancedThrottleLongFormatNewEndpointsMultiRotorNoSimple,
    advancedThrottleLongFormatNewEndpointsMultiRotor,
    advancedThrottleLongFormatNewEndpointsMultiRotorExternalGov,
    advancedThrottleLongFormatNewEndpointsMultiRotor2ExternalGov,
    advancedThrottleLongFormatNewEndpointsMultiRotorNoSimpleExternalGov,
    advancedThrottleLongFormatNewEndpointsMultiRotor2NoSimpleExternalGov,
    rx2Mode,
    multiRotorEndpoints,
    dynamicCurve1024,
    comboCheck,
    loggingDataEnabledV2,
    dyneticSystem,
    industrialThrottle,
    riniControllerRPM,
    resetLog,
    onTimeLogOperations,
    none
}

enum class DisplayType {
    none,
    advancedThrottleGroup,
    cheatActivationRange,
    checkBitField,
    checkboxOverlay,
    comboCheckbox,
    dataLogEnabled,
    dynamicCurve,
    linearSpinner,
    multiRotorEndpoints,
    readOnlyCheckbox,
    simpleCheckbox,
    simpleCombo,
    torqueLimit,
    voltageCutoffGroup
}
