package com.phelat.navigationresultsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp

class FragmentA : BundleFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("LOLLIPOP fragmentA")
        navigate(R.id.fragmentA_to_fragmentB, 1)
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)
        println("LOLLIPOP fragmentA requestCode: $requestCode - value: ${bundle.getString("FragmentB")}")
    }
}

class FragmentB : BundleFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("LOLLIPOP fragmentB")
        navigate(R.id.fragmentB_to_fragmentC, 2)
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)
        println("LOLLIPOP fragmentB requestCode: $requestCode - value: ${bundle.getString("FragmentC")}")
        navigateUp(1, Bundle().apply {
            putString("FragmentB", bundle.getString("FragmentC") + "Hey")
        })
    }
}

class FragmentC : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("LOLLIPOP fragmentC")
        navigateUp(2, Bundle().apply {
            putString("FragmentC", "Hey")
        })
    }
}