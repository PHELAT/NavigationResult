package com.phelat.navigationresult

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController

abstract class FragmentResultActivity : AppCompatActivity() {

    private var pendingResults = mutableMapOf<Int, Bundle?>()

    private var navHostFragmentIdCache: Int = -1

    private var backStackChangeListener: FragmentManager.OnBackStackChangedListener? = null

    private var destinationChangeListener: NavController.OnDestinationChangedListener? = null

    override fun onStart() {
        super.onStart()
        navHostFragmentIdCache = getNavHostFragmentId()
        attachDestinationChangeListener()
        attachBackStackChangeListener()
    }

    private fun attachDestinationChangeListener() {
        destinationChangeListener = NavController.OnDestinationChangedListener { _, _, arguments ->
            arguments?.getInt("fragment:resultCode", -1)
                ?.takeIf { it > -1 }
                ?.also { pendingResults[it] = null }
        }.also {
            findNavController(navHostFragmentIdCache).addOnDestinationChangedListener(it)
        }
    }

    private fun attachBackStackChangeListener() {
        backStackChangeListener = FragmentManager.OnBackStackChangedListener {
            (supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.primaryNavigationFragment as? BundleFragment)
                ?.takeIf { it.pendingResult > -1 }
                ?.takeIf { pendingResults[it.pendingResult] != null }
                ?.also {
                    it.onFragmentResult(it.pendingResult, pendingResults[it.pendingResult]!!)
                }
                ?.also { pendingResults[it.pendingResult] = null }
        }.also {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.addOnBackStackChangedListener(it)
        }
    }

    fun setBundle(resultCode: Int, bundle: Bundle) {
        pendingResults[resultCode] = bundle
    }

    override fun onStop() {
        super.onStop()
        backStackChangeListener?.let {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.removeOnBackStackChangedListener(it)
        }
        destinationChangeListener?.let {
            findNavController(navHostFragmentIdCache).removeOnDestinationChangedListener(it)
        }
    }

    abstract fun getNavHostFragmentId(): Int

}
