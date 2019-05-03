package com.phelat.navigationresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BundleFragment : Fragment() {

    var pendingResult: Int = -1

    open fun onFragmentResult(resultCode: Int, bundle: Bundle) {}

    fun navigate(navDirection: NavDirections, resultCode: Int = -1) {
        pendingResult = if (resultCode > -1) {
            navDirection.arguments.putInt("fragment:resultCode", resultCode)
            resultCode
        } else {
            navDirection.arguments.getInt("fragment:resultCode", -1)
        }
        findNavController().navigate(navDirection)
    }

}
