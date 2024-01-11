package com.kitelytech.castlelink.custom.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.app.Application
import com.kitelytech.castlelink.custom.models.DisplayType
import com.kitelytech.castlelink.custom.models.Product
import com.kitelytech.castlelink.custom.models.SettingGroup
import com.kitelytech.castlelink.custom.models.SettingValueSet
import com.kitelytech.castlelink.custom.utils.CoreApp
import com.kitelytech.castlelink.custom.views.*

class ProductSettingActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var mContext: Context

    private lateinit var product: Product
    private lateinit var settingGroup: SettingGroup
    private lateinit var valueSet: SettingValueSet

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_setting)

        mContext = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val extras = intent.extras
        if (extras != null) {
            val deviceName = extras.getString("DeviceName")
            val category = extras.getString("Category")
            deviceName?.let { category?.let { category -> parseSettingData(it, category) } }
        }

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Products"
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseSettingData(deviceName : String, category: String) {
        when (category) {
            "Surface" -> {
                for (item in Application.productData.sections[0].items) {
                    if (item.deviceName == deviceName) {
                        product = item.product!!
                        settingGroup = item.settingGroup!!
                    }
                }
            }
            "Air" -> {
                for (item in Application.productData.sections[1].items) {
                    if (item.deviceName == deviceName) {
                        product = item.product!!
                        settingGroup = item.settingGroup!!
                    }
                }
            }
            "Other" -> {
                for (item in Application.productData.sections[2].items) {
                    if (item.deviceName == deviceName) {
                        product = item.product!!
                        settingGroup = item.settingGroup!!
                    }
                }
            }
        }
        valueSet = settingGroup.createDefaultValueSet()
        Log.e("ProductSetting", "Setting Data was parsed")

        val settingView: LinearLayout = findViewById(R.id.settingView)

        // Add Product & Action View
        settingView.addView(ProductHeaderView(mContext, product))
        settingView.addView(ProductActionView(mContext, settingGroup, valueSet, false))

        // Add Product Setting Items
        addProductSettingItems(settingView)

        // Add Settings Help View
        settingView.addView(SettingHelpView(mContext, product))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addProductSettingItems(settingView: LinearLayout) {
        for (subgroup in settingGroup.displaySubgroups()) {
            val settingRowHeader = SettingRowHeader(mContext, subgroup.title.uppercase())
            settingView.addView(settingRowHeader)
            for (setting in subgroup.displaySettings()) {
                val settingValue = valueSet.values[setting.key]
                when (setting.displayType()) {
                    DisplayType.advancedThrottleGroup -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
                    DisplayType.cheatActivationRange -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
                    DisplayType.checkBitField -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
                    DisplayType.checkboxOverlay -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
                    DisplayType.comboCheckbox -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
                    DisplayType.dataLogEnabled -> {
                        settingView.addView(SettingRow(mContext, setting))
                    }
//                    DisplayType.dynamicCurve -> {
//                        SettingRowCurve(setting: setting, width: width)
//                    }
//                    DisplayType.linearSpinner -> {
//                        SettingRowSlider(setting: setting as! SettingLinearSpinner, settingValue: settingValue)
//                    }
//                    DisplayType.multiRotorEndpoints -> {
//                        SettingRow(setting: setting)
//                    }
                    DisplayType.readOnlyCheckbox -> {
                        settingView.addView(SettingRowSwitch(mContext, setting))
                    }
                    DisplayType.simpleCheckbox -> {
                        settingView.addView(SettingRowSwitch(mContext, setting))
                    }
//
//                    DisplayType.simpleCombo -> {
//                        SettingRowCombo(setting: setting, settingValue: settingValue, width: width)
//                    }
//                    DisplayType.torqueLimit -> {
//                        SettingRow(setting: setting)
//                    }
//                    DisplayType.voltageCutoffGroup -> {
//                        SettingRow(setting: setting)
//                    }
//                    else -> {
//                        break
//                    }
                }
//                when (setting.type) {
//                    "CutOffVoltage" -> {
//                        val cutOffVoltageView = CutOffVoltageView(mContext, component, getScreenWidth())
//                        settingView.addView(cutOffVoltageView)
//                    }
//                    "ComboBox" -> {
//                        val comboBoxView = ComboBoxView(mContext, component, getScreenWidth())
//                        settingView.addView(comboBoxView)
//                    }
//                    "Slider" -> {
//                        val sliderView = SliderView(mContext, component)
//                        settingView.addView(sliderView)
//                    }
//
//                    "Torque" -> {
//                        val torqueView = TorqueView(mContext, component)
//                        settingView.addView(torqueView)
//                    }
//                    "Curve" -> {
//                        val curveView = CurveView(mContext, component)
//                        settingView.addView(curveView)
//                    }
//                    else -> {
//                        print("Nothing type to add the component")
//                    }
//                }
            }
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}