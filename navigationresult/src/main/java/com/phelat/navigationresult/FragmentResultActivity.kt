package com.phelat.navigationresult

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController

abstract class FragmentResultActivity : AppCompatActivity() {

    private var pendingRequests = mutableMapOf<Int, Bundle?>()

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
            arguments?.getInt(Constants.FRAGMENT_REQUEST_CODE, -1)
                ?.takeIf { it > -1 }
                ?.also { pendingRequests[it] = null }
        }.also {
            findNavController(navHostFragmentIdCache).addOnDestinationChangedListener(it)
        }
    }

    private fun attachBackStackChangeListener() {
        backStackChangeListener = FragmentManager.OnBackStackChangedListener {
            (supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.primaryNavigationFragment as? BundleFragment)
                ?.takeIf { it.pendingRequest > -1 }
                ?.takeIf { pendingRequests[it.pendingRequest] != null }
                ?.also {
                    it.onFragmentResult(it.pendingRequest, pendingRequests[it.pendingRequest]!!)
                }
                ?.also { pendingRequests.remove(it.pendingRequest) }
        }.also {
            supportFragmentManager?.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.addOnBackStackChangedListener(it)
        }
    }

    internal fun setBundle(requestCode: Int, bundle: Bundle) {
        pendingRequests[requestCode] = bundle
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
