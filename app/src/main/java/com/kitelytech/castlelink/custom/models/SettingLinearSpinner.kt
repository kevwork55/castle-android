package com.kitelytech.castlelink.custom.models

import android.util.Log
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.round

class SettingLinearSpinner(
    var decimalDigits : Int = 0,
    var increment : Double = 0.0,
    var maximum: Double = 0.0,
    var minimum: Double = 0.0,
    var unitOfMeasurement: String = "",
    var displayValueMax: Double = 0.0,
    var displayValueMin: Double = 0.0,
    var rawValueMax: Int = 0,
    var rawValueMin: Int = 0
) {
    constructor(options: MutableList<SettingOption>?, settingMap: Map<String, Any>) : this() {
        // parse the setting map
        for ((settingKey, settingValue ) in settingMap) {
            if (!decodeKey(settingKey.lowercase(), settingValue)) {
                Log.e("Key", "Found unhandled key")
            }
        }

        options?.forEach { option ->
            val numericTitle = option.numericTitle
            displayValueMax = max(displayValueMax, numericTitle)
            displayValueMin = min(displayValueMin, numericTitle)
            rawValueMax = kotlin.math.max(rawValueMax, option.value)
            rawValueMin = kotlin.math.min(rawValueMin , option.value)
        }
    }

    private fun decodeKey(key: String, value: Any) : Boolean {
        when (key) {
            "dec" -> {
                // decimal digits (integer)
                this.decimalDigits = value.toString().toInt()
                return true
            }
            "inc" -> {
                // increment (float)
                this.increment = value.toString().toDouble()
                return true
            }
            "max" -> {
                // maximum value (float)
                this.maximum = value.toString().toDouble()
                return true
            }
            "min" -> {
                // minimum value (float)
                this.minimum = value.toString().toDouble()
                return true
            }
            "uom" -> {
                // unit of measure (string)
                this.unitOfMeasurement = value.toString()
                return true
            }
            else -> return false
        }
    }

    fun rawValue(value : Double) : Int {
        val displayValueMax = this.displayValueMax
        val displayValueMin = this.displayValueMin
        val rawValueMax = this.rawValueMax
        val rawValueMin = this.rawValueMin

        // clamp the display value
        val displayValue = clampDisplayValue(value)

        // calculate the raw value
        val scale = (displayValue - displayValueMin) / (displayValueMax - displayValueMin)
        val rawValue = (scale * (rawValueMax.toDouble() - rawValueMin.toDouble()) + rawValueMin.toDouble()).toInt()
        return rawValue
    }

    private fun clampDisplayValue(value: Double) : Double {
        var displayValue = value

        // round to the nearest increment
        displayValue = round(displayValue / increment) * increment
        // clamp to the minimum and maximum
        if (displayValue >= maximum) {
            displayValue = maximum
        }

        if (displayValue <= minimum) {
            displayValue = minimum
        }

        return displayValue
    }
}
