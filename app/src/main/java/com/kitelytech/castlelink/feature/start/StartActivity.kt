package com.kitelytech.castlelink.feature.start

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.presentation.ui.base.activity.BaseActivity
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.util.Constant.START_FROM_LOGIN_SCREEN_EXTRA_KEY
import com.kitelytech.castlelink.core.presentation.util.NavigationUtil.addFragment
import com.kitelytech.castlelink.core.presentation.util.NavigationUtil.replaceFragment
import com.kitelytech.castlelink.databinding.ActivityStartBinding
import com.kitelytech.castlelink.feature.home.HomeActivity
import com.kitelytech.castlelink.feature.start.auth.AuthManagerFragment
import com.kitelytech.castlelink.feature.start.auth.login.LoginCallback
import com.kitelytech.castlelink.feature.start.forgotpassword.ForgotPasswordCallback
import com.kitelytech.castlelink.feature.start.forgotpassword.ForgotPasswordFragment
import com.kitelytech.castlelink.feature.start.splash.SplashCallback
import com.kitelytech.castlelink.feature.start.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding, BaseViewModel>(),
    SplashCallback,
    LoginCallback,
    ForgotPasswordCallback,
    StartCallback {

    override val viewModelKClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun inflateBinding() = ActivityStartBinding.inflate(LayoutInflater.from(this))

    override fun configureBinding(savedInstanceState: Bundle?) {
        val isStartFromLogin = intent.getBooleanExtra(START_FROM_LOGIN_SCREEN_EXTRA_KEY, false)
        if (isStartFromLogin) {
            startAuthFragment()
        } else {
            startSplashFragment()
        }
    }

    private fun changeLoadingState(isLoading: Boolean) = with(binding) {
        if (isLoading) loadingAs.showAndStart() else loadingAs.hideAndStop()
    }

    override fun onSplashFinish(isUserAuthorized: Boolean) {
        if (isUserAuthorized) {
            startHomeScreen()
        } else {
            startAuthFragment()
        }
    }

    override fun setLoadingState(isLoading: Boolean) {
        changeLoadingState(isLoading)
    }

    override fun onCloseForgotPassword() {
        supportFragmentManager.popBackStack()
        hideKeyboard(binding.root)
    }

    override fun showForgotPassword() {
        addFragment(ForgotPasswordFragment.newInstance(), R.id.start_fcv)
        hideKeyboard(binding.root)
    }

    private fun startAuthFragment() {
        replaceFragment(AuthManagerFragment.newInstance(), R.id.start_fcv)
    }

    private fun startSplashFragment() {
        replaceFragment(SplashFragment.newInstance(), R.id.start_fcv)
    }

    override fun startHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }
}