package com.phelat.navigationresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BundleFragment : Fragment() {

    var pendingResult: Int = -1

    open fun onFragmentResult(resultCode: Int, bundle: Bundle) {}

    fun navigate(navDirection: NavDirections) {
        pendingResult = navDirection.arguments.getInt("fragment:resultCode", -1)
        findNavController().navigate(navDirection)
    }

}
