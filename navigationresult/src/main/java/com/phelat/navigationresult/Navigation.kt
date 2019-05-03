package com.phelat.navigationresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigateUp(resultCode: Int, bundle: Bundle) {
    (activity as? FragmentResultActivity)?.setBundle(resultCode, bundle)
    findNavController().navigateUp()
}
