package com.phelat.navigationresult

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BundleFragment : Fragment() {

    open fun onFragmentResult(resultCode: Int, bundle: Bundle) {}

}
