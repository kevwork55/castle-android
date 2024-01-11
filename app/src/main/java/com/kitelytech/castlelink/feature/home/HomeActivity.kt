package com.kitelytech.castlelink.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kitelytech.castlelink.core.presentation.ui.base.activity.BaseActivity
import com.kitelytech.castlelink.core.presentation.util.Constant.START_FROM_LOGIN_SCREEN_EXTRA_KEY
import com.kitelytech.castlelink.databinding.ActivityHomeBinding
import com.kitelytech.castlelink.feature.start.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    override val viewModelKClass: KClass<HomeViewModel>
        get() = HomeViewModel::class

    override fun inflateBinding() = ActivityHomeBinding.inflate(LayoutInflater.from(this))

    override fun configureBinding(savedInstanceState: Bundle?) = with(binding) {
        logOut.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this@HomeActivity, StartActivity::class.java).apply {
                putExtra(START_FROM_LOGIN_SCREEN_EXTRA_KEY, true)
            })
            finishAffinity()
        }
//        home.setOnClickListener { viewModel.parse() }
    }
}