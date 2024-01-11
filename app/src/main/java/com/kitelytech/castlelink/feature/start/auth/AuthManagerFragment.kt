package com.kitelytech.castlelink.feature.start.auth

import android.view.LayoutInflater
import android.widget.AdapterView
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.core.presentation.ui.base.viewmodel.BaseViewModel
import com.kitelytech.castlelink.core.presentation.util.NavigationUtil.addFragment
import com.kitelytech.castlelink.core.presentation.util.NavigationUtil.findAndHide
import com.kitelytech.castlelink.core.presentation.util.NavigationUtil.findAndShow
import com.kitelytech.castlelink.databinding.FragmentAuthManagerBinding
import com.kitelytech.castlelink.feature.start.auth.login.LoginFragment
import com.kitelytech.castlelink.feature.start.auth.registration.RegistrationFragment
import kotlin.reflect.KClass

class AuthManagerFragment : BaseFragment<FragmentAuthManagerBinding, BaseViewModel>() {
    override val viewModelKClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun inflateBinding() =
        FragmentAuthManagerBinding.inflate(LayoutInflater.from(requireContext()))

    override fun initialiseFragment() = with(binding) {
        configureScreenNavigation()
    }

    private fun configureScreenNavigation()  = with(binding) {
        addFragment(LoginFragment.newInstance(), R.id.auth_fcv)
        addFragment(RegistrationFragment.newInstance(), R.id.auth_fcv, isShown = false)
        loginRegisterMsv.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                when (position) {
                    0 -> {
                        findAndShow(LoginFragment.TAG)
                        findAndHide(RegistrationFragment.TAG)
                        hideKeyboard(requireView())
                    }
                    1 -> {
                        findAndShow(RegistrationFragment.TAG)
                        findAndHide(LoginFragment.TAG)
                        hideKeyboard(requireView())
                    }
                }
            }
    }

    companion object {
        val TAG: String = AuthManagerFragment::class.java.simpleName
        fun newInstance(): AuthManagerFragment {
            return AuthManagerFragment()
        }
    }
}