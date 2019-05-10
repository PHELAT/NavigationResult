package com.phelat.navigationresult

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BundleFragment : Fragment() {

    var pendingRequest: Int = -1

    open fun onFragmentResult(requestCode: Int, bundle: Bundle) {}

    fun navigate(navDirection: NavDirections, requestCode: Int = -1) {
        pendingRequest = if (requestCode > -1) {
            navDirection.arguments.putInt(Constants.FRAGMENT_REQUEST_CODE, requestCode)
            requestCode
        } else {
            navDirection.arguments.getInt(Constants.FRAGMENT_REQUEST_CODE, -1)
        }
        findNavController().navigate(navDirection)
    }

    fun navigate(@IdRes navDirection: Int, requestCode: Int) {
        pendingRequest = requestCode
        findNavController().navigate(navDirection, Bundle().apply {
            putInt(Constants.FRAGMENT_REQUEST_CODE, requestCode)
        })
    }

}
