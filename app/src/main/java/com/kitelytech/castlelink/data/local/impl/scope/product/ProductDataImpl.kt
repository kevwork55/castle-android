package com.kitelytech.castlelink.data.local.impl.scope.product

import android.content.Context
import android.util.Log
import com.kitelytech.castlelink.data.local.api.model.product.*
import com.kitelytech.castlelink.data.local.api.scope.product.ProductData
import org.json.JSONObject
import javax.inject.Inject


class ProductDataImpl @Inject constructor(private val context: Context) : ProductData {

    fun JSONObject.getIntOrNull(key: String): Int? {
        return try {
            getInt(key)
        } catch (e: Exception) {
            null
        }
    }

    fun JSONObject.getStringOrNull(key: String): String? {
        return try {
            getString(key)
        } catch (e: Exception) {
            null
        }
    }

    fun JSONObject.getDoubleOrNull(key: String): Double? {
        return try {
            getDouble(key)
        } catch (e: Exception) {
            null
        }
    }

    fun JSONObject.getBooleanFromInt(key: String): Boolean {
        val intRepresentation: Int = try {
            getInt(key)
        } catch (e: Exception) {
            0
        }
        return when (intRepresentation) {
            0 -> false
            1 -> true
            else -> throw ClassCastException("Cannot cast int = $intRepresentation to Boolean")
        }
    }

    fun JSONObject.getStringListFromString(key: String): List<String> {
        val string = try {
            getString(key)
        } catch (e: Exception) {
            return emptyList()
        }
        return string.split(",").map { it.trim() }
    }

    fun JSONObject.getJSONObjectOrNull(key: String): JSONObject? {
        return try {
            getJSONObject(key)
        } catch (e: Exception) {
            return null
        }
    }

    private fun parseThrottleResponse(
        jsonObject: JSONObject?,
        settingUid: String
    ): List<ThrottleResponse> {
        val throttleResponseList = mutableListOf<ThrottleResponse>()
        jsonObject?.let {
            val optsListJSONObject = it.getJSONObjectOrNull("Opts")

            val options = mutableListOf<Option>()

            optsListJSONObject?.keys()?.forEach { optsUid ->
                val optionJSONObject =
                    optsListJSONObject.getJSONObjectOrNull(optsUid)

                optionJSONObject?.let { JSONObject ->
                    val option = with(JSONObject) {
                        Option(
                            uid = optsUid,
                            idx = getIntOrNull("Idx"),
                            def = getIntOrNull("Def"),
                            value = getIntOrNull("Val"),
                            ttl = getIntOrNull("Ttl"),
                            hlp = getIntOrNull("Hlp"),
                            nmTtl = getDoubleOrNull("NmTtl"),
                            dis = getIntOrNull("Dis"),
                            dsblsList = emptyList(),
                        )
                    }
                    options.add(option)
                }
            }

            val throttleResponseModel = with(it) {
                ThrottleResponse(
                    settingUid = settingUid,
                    type = getIntOrNull("Type"),
                    idx = getIntOrNull("Idx"),
                    ttl = getIntOrNull("Ttl"),
                    help = getIntOrNull("Help"),
                    add = getIntOrNull("Add"),
                    dsz = getIntOrNull("DSz"),
                    dataType = getIntOrNull("DTyp"),
                    memArea = getIntOrNull("MemArea"),
                    min = getIntOrNull("Min"),
                    max = getIntOrNull("Max"),
                    inc = getIntOrNull("Inc"),
                    opts = options,
                )
            }
            throttleResponseList.add(throttleResponseModel)
        }
        return throttleResponseList
    }

    @Suppress("UNCHECKED_CAST")
    override fun parseProductData() {
        val jsonString = context.assets.open("ProductData.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(jsonString)


        val multiLangList = mutableListOf<MultiLang>()
        val connectHelpList = mutableListOf<ConnectHelp>()
        val controllerList = mutableListOf<Controller>()
        val settingGroupList = mutableListOf<SettingGroup>()
        val lookUpList = mutableListOf<LookUp>()

        val multiLangFields: MutableSet<SETTING> = mutableSetOf()

        val multiLangObject = jsonObject.getJSONObject("MultiLang")
        multiLangObject.keys().forEach { uid ->

            val multiLang = multiLangObject.getJSONObject(uid)
            val multiLangModel = MultiLang(
                uid = uid,
                key = multiLang.getIntOrNull("Key"),
                en = multiLang.getStringOrNull("en"),
                id = multiLang.getStringOrNull("ID"),
                de = multiLang.getStringOrNull("de"),
                zh = multiLang.getStringOrNull("zh"),
                cs = multiLang.getStringOrNull("cs"),
            )
            multiLangList.add(multiLangModel)


            multiLang.keys().forEach {

                multiLangFields.add(
                    SETTING(
                        it,
                        multiLang.get(it).javaClass.simpleName
                    )
                )
            }
        }
        Log.d("RAPSED_LISTS", "MULTI_LANG")
        multiLangList.forEach {
            Log.d("RAPSED_LISTS", "model = $it")
        }


        Log.d("TEST_PARSER", "MULTI LANG")
        multiLangFields.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        val ConnectHelpFields: MutableSet<SETTING> = mutableSetOf()
        val ConnectHelpObject = jsonObject.getJSONObject("ConnectHelp")

        ConnectHelpObject.keys().forEach { uid ->
            val connectHelpField = ConnectHelpObject.getJSONObject(uid)

            val connectHelpModel = ConnectHelp(
                uid = uid,
                deviceName = connectHelpField.getStringOrNull("device_name"),
                ghostDevice = connectHelpField.getStringOrNull("ghost_device"),
                requireLinkDevice = connectHelpField.getBooleanFromInt("requires_link_device"),
                mobileEnabled = connectHelpField.getBooleanFromInt("mobile_enabled"),
                versionList = connectHelpField.getStringListFromString("version_list"),
            )
            connectHelpList.add(connectHelpModel)


            connectHelpField.keys().forEach {
                ConnectHelpFields.add(
                    SETTING(
                        it,
                        connectHelpField.get(it).javaClass.simpleName
                    )
                )
            }
        }


        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "CONNECT HELP")
        ConnectHelpFields.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }


        val ControllersObject = jsonObject.getJSONObject("Controllers")
        val ControllersFields: MutableSet<SETTING> = mutableSetOf()
        val SOFTWAREFields: MutableSet<SETTING> = mutableSetOf()


        ControllersObject.keys().forEach { controllerUid ->

            if (controllerUid == "LOOKUP") {
                val lookupListJSONObject = ControllersObject.getJSONObject(controllerUid)
                lookupListJSONObject.keys().forEach { lookUpUid ->
                    val lookUpJSONObject = lookupListJSONObject.getJSONObject(lookUpUid)
                    val lookUpModel = with(lookUpJSONObject) {
                        LookUp(
                            uid = lookUpUid,
                            CtrlKey = getStringOrNull("CtrlKey"),
                            DemoGrpKey = getIntOrNull("DemoGrpKey"),
                        )
                    }
                    lookUpList.add(lookUpModel)
                }

            } else {
                val controllerField = ControllersObject.getJSONObject(controllerUid)


                val softwareList = mutableListOf<Software>()

                val softwareJsonObjects = try {
                    controllerField.getJSONObject("Software")
                } catch (e: Exception) {
                    return@forEach
                }

                softwareJsonObjects.keys().forEach { softwareUid ->
                    val softwareJsonObject = softwareJsonObjects.getJSONObject(softwareUid)
                    val softwareModel = Software(
                        uid = softwareUid,
                        versionId = softwareJsonObject.getStringOrNull("VerID"),
                        custVer = softwareJsonObject.getStringOrNull("CustVer"),
                        settingGroupKey = softwareJsonObject.getIntOrNull("SetGrpKey"),
                        isBeta = softwareJsonObject.getBooleanFromInt("Beta"),
                        isActive = softwareJsonObject.getBooleanFromInt("Active"),
                        isProduction = softwareJsonObject.getBooleanFromInt("Production"),
                        isBroken = softwareJsonObject.getBooleanFromInt("Broken"),
                        relDate = softwareJsonObject.getStringOrNull("RelDate"),
                        revText = softwareJsonObject.getIntOrNull("RevText"),
                        milestone = softwareJsonObject.getBooleanFromInt("Beta"),
                        resetLink = softwareJsonObject.getBooleanFromInt("Beta"),
                    )
                    softwareList.add(softwareModel)
                }

                controllerField.keys().forEach { key ->
                    if (key == "Software") {
                        val softwareObject = controllerField.getJSONObject(key)
                        softwareObject.keys().forEach { sowtwareUID ->
                            softwareObject.getJSONObject(sowtwareUID).keys()
                                .forEach { sowtwareKey ->
                                    SOFTWAREFields.add(
                                        SETTING(
                                            sowtwareKey,
                                            softwareObject.getJSONObject(sowtwareUID)
                                                .get(sowtwareKey).javaClass.simpleName
                                        )
                                    )
                                }
                        }
                    }
                    ControllersFields.add(
                        SETTING(
                            key,
                            controllerField.get(key).javaClass.simpleName
                        )
                    )
                }

                val controllerModel = Controller(
                    uid = controllerUid,
                    name = controllerField.getStringOrNull("Name"),
                    productClass = controllerField.getStringOrNull("productClass"),
                    isMobileEnabled = controllerField.getBooleanFromInt("mobile_enabled"),
                    minVolt = controllerField.getDoubleOrNull("productClass"),
                    maxVolt = controllerField.getDoubleOrNull("productClass"),
                    softwareList = softwareList,
                )
                controllerList.add(controllerModel)

            }
        }
        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "CONTROLLER")
        ControllersFields.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "SOWTWARE")
        SOFTWAREFields.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }


        val settingGroupsJSONObject = jsonObject.getJSONObject("SettingGroups")
        val settingsGroupFields: MutableSet<String> = mutableSetOf()

        val subSettingsFileds: MutableSet<SETTING> = mutableSetOf()

        val settingsFileds: MutableSet<SETTING> = mutableSetOf()

        val optionsFileds: MutableSet<SETTING> = mutableSetOf()

        val dsblFileds: MutableSet<SETTING> = mutableSetOf()

        val warnsFileds: MutableSet<SETTING> = mutableSetOf()

        val throtleFileds: MutableSet<SETTING> = mutableSetOf()

        val throtleOptFileds: MutableSet<SETTING> = mutableSetOf()

        val govnerGaiFileds: MutableSet<SETTING> = mutableSetOf()

        val spoolUpFileds: MutableSet<SETTING> = mutableSetOf()

        val spooledUpFileds: MutableSet<SETTING> = mutableSetOf()

        settingGroupsJSONObject.keys().forEach { settingGroupUid ->
            val settingGroupJSONObject = settingGroupsJSONObject.getJSONObject(settingGroupUid)

            val subSettingGroupList = mutableListOf<SubSettingGroup>()


            settingGroupJSONObject.keys().forEach { subSettingGroupUid ->


                if (subSettingGroupUid != "Defaults") {
                    val subSettingJSONObject =
                        settingGroupJSONObject.getJSONObject(subSettingGroupUid)

                    val settingList = mutableListOf<Setting>()

                    val settingsJSONObject = subSettingJSONObject.getJSONObject("Sets")

                    settingsJSONObject.keys().forEach { settingUid ->
                        val settingJSONObject = settingsJSONObject.getJSONObject(settingUid)

                        val optionList = mutableListOf<Option>()
                        val warnList = mutableListOf<Warn>()
//                        val throttleResponseList = mutableListOf<ThrottleResponse>()
//                        val governorGainList = mutableListOf<ThrottleResponse>()
//                        val spoolUpList = mutableListOf<ThrottleResponse>()
//                        val spooledUpList = mutableListOf<ThrottleResponse>()

                        val optsListJSONObject = settingJSONObject.getJSONObjectOrNull("Opts")
                        val warnListJSONObject = settingJSONObject.getJSONObjectOrNull("Warns")
                        val throttleResponseListJSONObject =
                            settingJSONObject.getJSONObjectOrNull("THROTTLE_RESPONSE")
                        val governorGainListJSONObject =
                            settingJSONObject.getJSONObjectOrNull("GOVERNOR_GAIN")
                        val spoolUpListJSONObject =
                            settingJSONObject.getJSONObjectOrNull("SPOOL_UP")
                        val spooledUpListJSONObject =
                            settingJSONObject.getJSONObjectOrNull("SPOOLED_UP")

                        optsListJSONObject?.keys()?.forEach { optionUid ->
                            val optionJSONObject = optsListJSONObject.getJSONObject(optionUid)

                            val dsblsList = mutableListOf<Dsbls>()

                            val dsblsListJSONObject = optionJSONObject.getJSONObjectOrNull("Dsbls")
                            dsblsListJSONObject?.keys()?.forEach { dsblsUid ->
                                val dsblsJSONObject = dsblsListJSONObject.getJSONObject(dsblsUid)
                                val dsbls = with(dsblsJSONObject) {
                                    Dsbls(
                                        uid = dsblsUid,
                                        type = getIntOrNull("Type"),
                                        sKey = getStringOrNull("SKey"),
                                    )
                                }
                                dsblsList.add(dsbls)
                            }

                            val option = with(optionJSONObject) {
                                Option(
                                    uid = optionUid,
                                    idx = getIntOrNull("Idx"),
                                    def = getIntOrNull("Def"),
                                    value = getIntOrNull("Val"),
                                    ttl = getIntOrNull("Ttl"),
                                    hlp = getIntOrNull("Hlp"),
                                    nmTtl = getDoubleOrNull("NmTtl"),
                                    dis = getIntOrNull("Dis"),
                                    dsblsList = dsblsList,
                                )
                            }
                            optionList.add(option)
                        }

                        warnListJSONObject?.keys()?.forEach { warnUid ->
                            val warnJSONObject = warnListJSONObject.getJSONObject(warnUid)
                            val warnModel = with(warnJSONObject) {
                                Warn(
                                    uid = warnUid,
                                    type = getIntOrNull("Type"),
                                    value = getIntOrNull("Value"),
                                    text = getIntOrNull("Text"),
                                )
                            }
                            warnList.add(warnModel)
                        }

                        val throttleResponseList =
                            parseThrottleResponse(throttleResponseListJSONObject, settingUid)
                        val governorGainList =
                            parseThrottleResponse(governorGainListJSONObject, settingUid)
                        val spoolUpList = parseThrottleResponse(spoolUpListJSONObject, settingUid)
                        val spooledUpList =
                            parseThrottleResponse(spooledUpListJSONObject, settingUid)

                        val setting = with(settingJSONObject) {
                            Setting(
                                uid = settingUid,
                                type = getIntOrNull("Type"),
                                idx = getIntOrNull("Idx"),
                                ttl = getIntOrNull("Ttl"),
                                help = getIntOrNull("Help"),
                                add = getIntOrNull("Add"),
                                dsz = getIntOrNull("DSz"),
                                dataType = getIntOrNull("DTyp"),
                                memArea = getIntOrNull("MemArea"),
                                min = getDoubleOrNull("Min"),
                                max = getDoubleOrNull("Max"),
                                inc = getDoubleOrNull("Inc"),
                                uom = getStringOrNull("UOM"),
                                dataFreq = getIntOrNull("DATALOG_FREQ"),
                                armCountMin = getIntOrNull("ARM_COUNT_MIN"),
                                armCountDefault = getIntOrNull("ARM_COUNT_DEFAULT"),
                                armCountMax = getIntOrNull("ARM_COUNT_MAX"),
                                maxCountMin = getIntOrNull("MAX_COUNT_MIN"),
                                maxCountDefault = getIntOrNull("MAX_COUNT_DEFAULT"),
                                maxCountMax = getIntOrNull("MAX_COUNT_MAX"),
                                clockFreq = getIntOrNull("CLOCK_FREQ"),
                                divider = getIntOrNull("DIVIDER"),
                                options = optionList,
                                warns = warnList,
                                throttleResponses = throttleResponseList,
                                governorGain = governorGainList,
                                spoolUp = spoolUpList,
                                spooledUp = spooledUpList,
                            )
                        }
                        settingList.add(setting)
                    }

                    val subSettingGroup = SubSettingGroup(
                        uid = subSettingGroupUid,
                        idx = subSettingJSONObject.getIntOrNull("Idx"),
                        ttl = subSettingJSONObject.getIntOrNull("Ttl"),
                        hid = subSettingJSONObject.getBooleanFromInt("Hid"),
                        settings = settingList,
                    )
                    subSettingGroupList.add(subSettingGroup)
                }


            }

            val settingGroup = SettingGroup(
                uid = settingGroupUid,
                defaults = settingGroupJSONObject.getStringOrNull("Defaults"),
                subSettingsGroup = subSettingGroupList,
            )
            settingGroupList.add(settingGroup)



            settingGroupJSONObject.keys().forEach { subSettingGroupFieldKey ->
                if (subSettingGroupFieldKey != "Defaults") {
                    val subSetting = settingGroupJSONObject.getJSONObject(subSettingGroupFieldKey)
                    subSetting.keys().forEach { sybSettingKey ->
                        if (sybSettingKey == "Sets") {
                            val settingSet = subSetting.getJSONObject(sybSettingKey)
                            settingSet.keys().forEach { settingSetKey ->
                                val setting = settingSet.getJSONObject(settingSetKey)
                                setting.keys().forEach { settingKey ->
                                    settingsFileds.add(
                                        SETTING(
                                            settingKey,
                                            setting.get(settingKey).javaClass.simpleName
                                        )
                                    )

                                    if (setting.get(settingKey).javaClass.simpleName == "JSONObject") {
                                        val jsonObj = setting.getJSONObject(settingKey)
                                        if (settingKey == "Opts") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                val option = jsonObj.getJSONObject(optionObjKey)
                                                option.keys().forEach { optionKey ->
                                                    if (optionKey == "Dsbls") {
                                                        val dsbl = option.getJSONObject("Dsbls")
                                                        dsbl.keys().forEach { dsblKey ->
                                                            dsblFileds.add(
                                                                SETTING(
                                                                    dsblKey,
                                                                    option.get(optionKey).javaClass.simpleName
                                                                )
                                                            )
                                                        }
                                                    }

                                                    optionsFileds.add(
                                                        SETTING(
                                                            optionKey,
                                                            option.get(optionKey).javaClass.simpleName
                                                        )
                                                    )
                                                }

                                            }
                                        }

                                        if (settingKey == "Warns") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                val option = jsonObj.getJSONObject(optionObjKey)
                                                option.keys().forEach { optionKey ->
                                                    warnsFileds.add(
                                                        SETTING(
                                                            optionKey,
                                                            option.get(optionKey).javaClass.simpleName
                                                        )
                                                    )
                                                }

                                            }
                                        }



                                        if (settingKey == "THROTTLE_RESPONSE") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                throtleFileds.add(
                                                    SETTING(
                                                        optionObjKey,
                                                        jsonObj.get(optionObjKey).javaClass.simpleName
                                                    )
                                                )

                                                if (optionObjKey == "Opts") {
                                                    val optObj = jsonObj.getJSONObject(optionObjKey)
                                                    optObj.keys().forEach { iy ->
                                                        val option = optObj.getJSONObject(iy)
                                                        option.keys().forEach { optionKey ->
                                                            throtleOptFileds.add(
                                                                SETTING(
                                                                    optionKey,
                                                                    option.get(optionKey).javaClass.simpleName
                                                                )
                                                            )
                                                        }

                                                    }

                                                }
                                            }
                                        }



                                        if (settingKey == "GOVERNOR_GAIN") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                govnerGaiFileds.add(
                                                    SETTING(
                                                        optionObjKey,
                                                        jsonObj.get(optionObjKey).javaClass.simpleName
                                                    )
                                                )
                                            }
                                        }

                                        if (settingKey == "SPOOL_UP") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                spoolUpFileds.add(
                                                    SETTING(
                                                        optionObjKey,
                                                        jsonObj.get(optionObjKey).javaClass.simpleName
                                                    )
                                                )
                                            }
                                        }



                                        if (settingKey == "SPOOLED_UP") {
                                            jsonObj.keys().forEach { optionObjKey ->
                                                spooledUpFileds.add(
                                                    SETTING(
                                                        optionObjKey,
                                                        jsonObj.get(optionObjKey).javaClass.simpleName
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        subSettingsFileds.add(
                            SETTING(
                                sybSettingKey,
                                subSetting.get(sybSettingKey).javaClass.simpleName
                            )
                        )
                    }
                }
                settingsGroupFields.add(subSettingGroupFieldKey)
            }
        }

        val databaseInfoJSONObject = jsonObject.getJSONObject("DatabaseInfo")
        val databaseInfo = with(databaseInfoJSONObject) {
            DatabaseInfo(
                databaseVersion = getIntOrNull("databaseVersion"),
                requiredAppVersion = getDoubleOrNull("requiredAppVersion"),
            )
        }

        val productData = ProductDataModel(
            multiLang = multiLangList,
            controllers = controllerList,
            lookUpList = lookUpList,
            settingsGroup = settingGroupList,
            connectHelpList = connectHelpList,
            databaseInfo = databaseInfo,
        )

        Log.d("SHOW_PARSED_DAA", "lookUpList = $lookUpList")

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "Settings Group")
        settingsGroupFields.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "Sub Settings Group")
        subSettingsFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "Settings")
        settingsFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }


        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "OPTIONS")
        optionsFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "WARNS")
        warnsFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "DSBL")
        dsblFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }


        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "THROTLE RESPONSE")
        throtleFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "THROTLE OPTION RESPONSE")
        throtleOptFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }



        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "GOVERNOR_GAIN")
        govnerGaiFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "SPOOL UP")
        spoolUpFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }

        Log.d("TEST_PARSER", "")
        Log.d("TEST_PARSER", "SPOOLED UP")
        spooledUpFileds.forEach {
            Log.d("TEST_PARSER", "field = $it")
        }


        Log.d("RAPSED_LISTS", "MULTI_LANG")
        multiLangList.forEach {
            Log.d("RAPSED_LISTS", "model = $it")
        }

        Log.d("RAPSED_LISTS", "")
        Log.d("RAPSED_LISTS", "CONNECT HELP")
        connectHelpList.forEach {
            Log.d("RAPSED_LISTS", "model = $it")
        }
    }

}

data class SETTING(
    val field: String,
    val type: String
) {
    override fun toString(): String {
        return "field=$field, type=$type"
    }
}