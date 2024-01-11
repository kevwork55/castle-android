package com.kitelytech.castlelink.custom.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.custom.models.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class CoreApp: Application() {

//    companion object {
//        lateinit var productSetting : ProductSetting
//        var productData : ProductData = ProductData(null, null)
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        parseProductData(this)
//        productSetting = getProductSettings(this)
//    }
//
//    private fun getProductSettings(context: Context) : ProductSetting {
//        lateinit var jsonString: String
//        try {
//            jsonString = context.assets.open("device_settings.json")
//                .bufferedReader()
//                .use { it.readText() }
//        } catch (ioException: IOException) {
//            Log.e("Error", ioException.localizedMessage!!)
//        }
//        return Gson().fromJson(jsonString, ProductSetting::class.java)
//    }
//
//    private fun parseProductData(context: Context) {
//        lateinit var jsonString: String
//
//        try {
//            jsonString = context.assets.open("ProductData.json")
//                .bufferedReader()
//                .use { it.readText() }
//            val jsonObject = JSONObject(jsonString)
//
//            // parse the database info
//            val databaseInfoObject = jsonObject.getJSONObject("DatabaseInfo")
//            val databaseVersion = databaseInfoObject.getString("databaseVersion")
//            val requiredAppVersion = databaseInfoObject.getString("requiredAppVersion")
//            productData.databaseInfo = DatabaseInfo(databaseVersion, requiredAppVersion)
//
//            // parse the multi language
//            val multiLangObject = jsonObject.getJSONObject("MultiLang")
//            val multiLangMap = HashMap<Int, String>()
//            jsonToMap(multiLangObject).map {
//                val entryData : Map<String, String> = it.value as Map<String, String>
//                val entryKey : Int = entryData["Key"].toString().toInt()
//                val entryString : String = entryData["en"].toString()
//
//                multiLangMap[entryKey] = entryString
//            }
//            productData.multiLang = multiLangMap
//
//            // parse the setting groups
//            val settingsGroupObject = jsonObject.getJSONObject("SettingGroups")
//            val settingGroupMaps = jsonToMap(settingsGroupObject)
//            val settingGroups = HashMap<Int, SettingGroup>()
//            for ((key, value ) in settingGroupMaps) {
//                val keyInteger = key.drop(1).toInt()
//                val settingGroupMap : Map<String, Any> = value as Map<String, Any>
//                val settingGroup = SettingGroup(settingGroupMap, productData)
//                settingGroups[keyInteger] = settingGroup
//            }
//            productData.settingGroups = settingGroups
//
//            // parse the products
//            val productObject = jsonObject.getJSONObject("Controllers")
//            val productMaps = jsonToMap(productObject)
//            val products = HashMap<String, Product>()
//            for ((key, value) in productMaps) {
//                if (key.lowercase() != "lookup") {
//                    val productMap : Map<String, Any> = value as Map<String, Any>
//                    products[key] = Product(productMap, productData)
//                } else {
//                    productData.firmwareLookup = value as Map<String, Map<String, Any>>
//                }
//            }
//            productData.products = products
//
//            // parse the connect help
//            val connectHelpObject = jsonObject.getJSONObject("ConnectHelp")
//            val connectHelpMaps = jsonToMap(connectHelpObject)
//            for ((_, value) in connectHelpMaps) {
//                val connectHelpMap : Map<String, Any> = value as Map<String, Any>
//                val connectHelp = ConnectHelp(connectHelpMap, productData)
//                productData.connectHelps.add(connectHelp)
//                if (connectHelp.demoFirmwareVersion.isNotEmpty()) {
//                    val map : Map<String, Any> = productData.firmwareLookup[connectHelp.demoFirmwareVersion]!!
//                    val controllerKey = map["CtrlKey"].toString()
//                    val settingGroupKey = map["DemoGrpKey"].toString().toInt()
//                    connectHelp.product = productData.products[controllerKey]
//                    connectHelp.settingGroup = productData.settingGroups[settingGroupKey]
//                }
//            }
//
//            // sort the products
//            val airProducts : MutableList<ConnectHelp> = mutableListOf()
//            val otherProducts : MutableList<ConnectHelp> = mutableListOf()
//            val surfaceProducts : MutableList<ConnectHelp> = mutableListOf()
//
//            // this should use product categories for sorting
//            for (item in productData.connectHelps) {
//                val deviceName = item.deviceName.lowercase()
//                if (deviceName.contains("mamba") || deviceName.contains("sidewinder") || deviceName.contains("copperhead") || deviceName.contains("cobra")) {
//                    surfaceProducts.add(item)
//                } else if (deviceName.contains("phoenix") || deviceName.contains("talon") || deviceName.contains("multi-rotor") || deviceName.contains("dmr")) {
//                    airProducts.add(item)
//                } else {
//                    otherProducts.add(item)
//                }
//            }
//
//            // create the sections
//            productData.connectionHelpSections.add(ConnectHelpSection(
//                isHeader = true,
//                isFooter = false,
//                name = context.getString(R.string.header_text),
//                subName = "",
//                drawable = R.drawable.castle_logo,
//                items = mutableListOf()
//            ))
//
//            if (surfaceProducts.size > 0) {
//                surfaceProducts.sortBy { it.deviceName }
//                productData.sections.add(Section(surfaceProducts))
//                val surfaces : MutableList<ConnectHelpName> = mutableListOf()
//                surfaceProducts.forEach {
//                    surfaces.add(ConnectHelpName(it.deviceName, "Surface"))
//                }
//                productData.connectionHelpSections.add(ConnectHelpSection(
//                    isHeader = false,
//                    isFooter = false,
//                    name = "Surface",
//                    subName = "Anything With Wheels",
//                    drawable = R.drawable.maxresdefault,
//                    items = surfaces
//                ))
//            }
//            if (airProducts.size > 0) {
//                airProducts.sortBy { it.deviceName }
//                productData.sections.add(Section(airProducts))
//                val airs : MutableList<ConnectHelpName> = mutableListOf()
//                airProducts.forEach {
//                    airs.add(ConnectHelpName(it.deviceName, "Air"))
//                }
//                productData.connectionHelpSections.add(ConnectHelpSection(
//                    isHeader = false,
//                    isFooter = false,
//                    name = "Air",
//                    subName = "Wings or Rotors",
//                    drawable = R.drawable.close_up_photo,
//                    items = airs
//                ))
//            }
//            if (otherProducts.size > 0) {
//                otherProducts.sortBy { it.deviceName }
//                productData.sections.add(Section(otherProducts))
//                val others : MutableList<ConnectHelpName> = mutableListOf()
//                otherProducts.forEach {
//                    others.add(ConnectHelpName(it.deviceName, "Other"))
//                }
//                productData.connectionHelpSections.add(ConnectHelpSection(
//                    isHeader = false,
//                    isFooter = false,
//                    name = "Other",
//                    subName = "Everything Else",
//                    drawable = R.drawable.everything_else,
//                    items = others
//                ))
//            }
//
//            productData.connectionHelpSections.add(ConnectHelpSection(
//                isHeader = false,
//                isFooter = true,
//                name = context.getString(R.string.footer_text),
//                subName = "",
//                drawable = R.drawable.castle_footer_logo,
//                items = mutableListOf()
//            ))
//
//        } catch (ioException: IOException) {
//            Log.e("Error", ioException.localizedMessage!!)
//        }
//    }
//
//    @Throws(JSONException::class)
//    fun jsonToMap(json: JSONObject): Map<String, Any> {
//        var retMap: Map<String, Any> = HashMap()
//        if (json !== JSONObject.NULL) {
//            retMap = toMap(json)
//        }
//        return retMap
//    }
//
//    @Throws(JSONException::class)
//    fun toMap(`object`: JSONObject): Map<String, Any> {
//        val map: MutableMap<String, Any> = HashMap()
//        val keysItr = `object`.keys()
//        while (keysItr.hasNext()) {
//            val key = keysItr.next()
//            var value = `object`[key]
//            if (value is JSONArray) {
//                value = toList(value)
//            } else if (value is JSONObject) {
//                value = toMap(value)
//            }
//            map[key] = value
//        }
//        return map
//    }
//
//    @Throws(JSONException::class)
//    fun toList(array: JSONArray): List<Any> {
//        val list: MutableList<Any> = ArrayList()
//        for (i in 0 until array.length()) {
//            var value = array[i]
//            if (value is JSONArray) {
//                value = toList(value)
//            } else if (value is JSONObject) {
//                value = toMap(value)
//            }
//            list.add(value.toString())
//        }
//        return list
//    }
}