package com.kitelytech.castlelink.custom.models

data class Product (
    var firmwareVersions: MutableList<Firmware> = mutableListOf(),
    var maxVolt: Double = 0.0,
    var minVolt: Double = 0.0,
    var mobileEnabled : Boolean = false,
    var name : String = "",
    var productClass : String = ""
) {
    constructor(productMap : Map<String, Any>, productData: ProductData) : this() {
        for ((key, value) in productMap) {
            when (key.lowercase()) {
                "name" -> {
                    // product name (string)
                    this.name = value.toString()
                }
                "productclass" -> {
                    // product class (string)
                    this.productClass = value.toString()
                }
                "maxvolt" -> {
                    // maximum voltage for the product (double)
                    this.maxVolt = value.toString().toDouble()
                }
                "minvolt" -> {
                    // minimum voltage for the product (double)
                    this.minVolt = value.toString().toDouble()
                }
                "mobile_enabled" -> {
                    // enable on mobile app flag (integer)
                    val mobileValue = value.toString().toInt()
                    this.mobileEnabled = (mobileValue != 0)
                }
                "software" -> {
                    // firmware versions (dictionary)
                    val firmwareMaps : Map<String, Any> = value as Map<String, Any>
                    for ((_, firmwareValue) in firmwareMaps) {
                        val firmwareMap : Map<String, Any> = firmwareValue as Map<String, Any>
                        this.firmwareVersions.add(Firmware(firmwareMap, productData))
                    }
                }
            }
        }
    }
}