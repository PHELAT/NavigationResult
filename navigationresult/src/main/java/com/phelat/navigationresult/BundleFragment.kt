package com.phelat.navigationresult

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

abstract class BundleFragment : Fragment() {

    internal var pendingRequest: Int = -1

    open fun onFragmentResult(requestCode: Int, bundle: Bundle) {}

    fun navigate(navDirection: NavDirections, requestCode: Int = -1) {
        navigate(navDirection.actionId, navDirection.arguments, requestCode)
    }

    fun navigate(
        navDirection: NavDirections,
        navOptions: NavOptions?,
        requestCode: Int = -1
    ) {
        navigate(navDirection.actionId, navDirection.arguments, navOptions, null, requestCode)
    }

    fun navigate(
        navDirection: NavDirections,
        navigatorExtras: Navigator.Extras?,
        requestCode: Int = -1
    ) {
        navigate(navDirection.actionId, navDirection.arguments, null, navigatorExtras, requestCode)
    }

    fun navigate(@IdRes navDirection: Int, requestCode: Int) {
        navigate(navDirection, null, requestCode)
    }

    fun navigate(@IdRes navDirection: Int, bundle: Bundle?, requestCode: Int) {
        navigate(navDirection, bundle, null, requestCode)
    }

    fun navigate(
        @IdRes navDirection: Int,
        bundle: Bundle?,
        navOptions: NavOptions?,
        requestCode: Int
    ) {
        navigate(navDirection, bundle, navOptions, null, requestCode)
    }

    fun navigate(
        @IdRes navDirection: Int,
        bundle: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?,
        requestCode: Int
    ) {
        val theActualBundle = bundle ?: Bundle()
        extractPendingRequest(theActualBundle, requestCode)
        findNavController().navigate(navDirection, theActualBundle, navOptions, navigatorExtras)
    }

    private fun extractPendingRequest(bundle: Bundle, requestCode: Int) {
        pendingRequest = if (requestCode > -1) {
            bundle.putInt(Constants.FRAGMENT_REQUEST_CODE, requestCode)
            requestCode
        } else {
            bundle.getInt(Constants.FRAGMENT_REQUEST_CODE, -1)
        }
    }

}
