package com.kitelytech.castlelink.core.presentation.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object NavigationUtil {
    fun FragmentActivity.replaceFragment(
        destinationFragment: Fragment,
        containerId: Int,
        isAddToBackStack: Boolean = false,
        replaceIfExist: Boolean = true
    ) {
        val tag = destinationFragment::class.java.simpleName
        val fragment = if (replaceIfExist) {
            supportFragmentManager.findFragmentByTag(tag) ?: destinationFragment
        } else destinationFragment

        supportFragmentManager.beginTransaction().apply {
            replace(
                containerId,
                fragment,
                tag
            )
            if (isAddToBackStack) addToBackStack(tag)
            commit()
        }
    }

    fun FragmentActivity.addFragment(
        destinationFragment: Fragment,
        containerId: Int,
        isAddToBackStack: Boolean = true,
        replaceIfExist: Boolean = true,
        isShown: Boolean = true
    ) {
        val tag = destinationFragment::class.java.simpleName
        val fragment = if (replaceIfExist) {
            supportFragmentManager.findFragmentByTag(tag) ?: destinationFragment
        } else destinationFragment

        supportFragmentManager.beginTransaction().apply {
            add(
                containerId,
                fragment,
                tag
            )
            if (isAddToBackStack) addToBackStack(tag)
            if (!isShown) hide(fragment)
            commit()
        }
    }

    fun Fragment.addFragment(
        destinationFragment: Fragment,
        containerId: Int,
        isAddToBackStack: Boolean = false,
        replaceIfExist: Boolean = true,
        isShown: Boolean = true
    ) {
        val tag = destinationFragment::class.java.simpleName
        val fragment = if (replaceIfExist) {
            childFragmentManager.findFragmentByTag(tag) ?: destinationFragment
        } else destinationFragment

        childFragmentManager.beginTransaction().apply {
            add(
                containerId,
                fragment,
                tag
            )
            if (isAddToBackStack) addToBackStack(tag)
            if (!isShown) hide(fragment)
            commit()
        }
    }

    fun Fragment.findAndShow(
        tag: String
    ) {
        childFragmentManager.findFragmentByTag(tag)?.let {
            childFragmentManager.beginTransaction().apply {
                show(it)
                commit()
            }
        }
    }

    fun Fragment.findAndHide(
        tag: String
    ) {
        childFragmentManager.findFragmentByTag(tag)?.let {
            childFragmentManager.beginTransaction().apply {
                hide(it)
                commit()
            }
        }
    }
}