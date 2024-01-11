package com.kitelytech.castlelink.custom.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.app.Application
import com.kitelytech.castlelink.core.presentation.ui.base.activity.BaseActivity
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.custom.adapters.HomeAdapter
import com.kitelytech.castlelink.custom.adapters.SectionClickListener
import com.kitelytech.castlelink.custom.utils.CoreApp
import com.kitelytech.castlelink.databinding.ActivityMainBinding
import com.kitelytech.castlelink.feature.start.auth.AuthManagerFragment
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val homeRecyclerView: RecyclerView = findViewById(R.id.home_recycler_view)
        val layoutManger = GridLayoutManager(mContext, 2)
        homeRecyclerView.layoutManager = layoutManger
        val homeAdapter = HomeAdapter(
            Application.productData.connectionHelpSections,
            layoutManger,
            SectionClickListener { section ->
                Log.d("MainActivity", section.name)
                val intent = Intent(mContext, ProductSettingActivity::class.java)
                intent.putExtra("DeviceName", section.name)
                intent.putExtra("Category", section.category)
                startActivity(intent)
            }
        )
        homeRecyclerView.adapter = homeAdapter
    }
}