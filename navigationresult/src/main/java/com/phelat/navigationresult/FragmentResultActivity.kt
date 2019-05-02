package com.phelat.navigationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

abstract class FragmentResultActivity : AppCompatActivity() {

    private var bundle: Bundle? = null

    private var navHostFragmentIdCache: Int = -1

    private var backStackChangeListener: FragmentManager.OnBackStackChangedListener? = null

    override fun onStart() {
        super.onStart()
        navHostFragmentIdCache = getNavHostFragmentId()
        backStackChangeListener = FragmentManager.OnBackStackChangedListener {
            if (bundle != null) {
                (supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                    ?.childFragmentManager
                    ?.primaryNavigationFragment as? BundleFragment)
                    ?.onFragmentResult(-1, bundle!!)
                    ?.also {
                        bundle = null
                    }
            }
        }.also {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.addOnBackStackChangedListener(it)
        }
    }

    fun setBundle(bundle: Bundle) {
        this.bundle = bundle
    }

    override fun onStop() {
        super.onStop()
        backStackChangeListener?.let {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.removeOnBackStackChangedListener(it)
        }
    }

    abstract fun getNavHostFragmentId(): Int

}
