package com.phelat.navigationresult

import android.os.Bundle
import androidx.annotation.IdRes
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

    fun updateNavHostFragmentId(@IdRes id: Int) {
        navHostFragmentIdCache = id
        reattach()
    }

    private fun reattach() {
        detachBackStackChangeListener()
        detachDestinationChangeListener()
        attachBackStackChangeListener()
        attachDestinationChangeListener()
    }

    override fun onStop() {
        super.onStop()
        detachBackStackChangeListener()
        detachDestinationChangeListener()
    }

    private fun detachBackStackChangeListener() {
        backStackChangeListener?.let {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.removeOnBackStackChangedListener(it)
        }
    }

    private fun detachDestinationChangeListener() {
        destinationChangeListener?.let {
            findNavController(navHostFragmentIdCache).removeOnDestinationChangedListener(it)
        }
    }

    @IdRes
    abstract fun getNavHostFragmentId(): Int

}
