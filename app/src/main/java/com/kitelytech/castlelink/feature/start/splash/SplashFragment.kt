package com.kitelytech.castlelink.feature.start.splash

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kitelytech.castlelink.R
import com.kitelytech.castlelink.core.presentation.model.RequestState
import com.kitelytech.castlelink.core.presentation.ui.base.fragment.BaseFragment
import com.kitelytech.castlelink.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {
    override val viewModelKClass: KClass<SplashViewModel>
        get() = SplashViewModel::class

    override fun inflateBinding() =
        FragmentSplashBinding.inflate(LayoutInflater.from(requireContext()))

    private var splashCallback: SplashCallback? = null

    override fun initialiseFragment() {
        viewModel.isUserSignedUp()
        observeValidationError()
    }

    private fun observeValidationError() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.requestStateFlow.collectLatest {
                when (it) {
                    is RequestState.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    RequestState.Idle -> {

                    }
                    RequestState.Processing -> {

                    }
                    is RequestState.Success -> {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        val isUserPresent = it.data as Boolean
                        splashCallback?.onSplashFinish(isUserPresent)
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SplashCallback) splashCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        splashCallback = null
    }

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName
        fun newInstance() = SplashFragment()
    }
}