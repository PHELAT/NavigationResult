package com.phelat.navigationresult

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController

abstract class FragmentResultActivity : AppCompatActivity() {

    private var pendingRequests = HashMap<Int, Bundle?>()

    private var navHostFragmentIdCache: Int = -1

    private var backStackChangeListener: FragmentManager.OnBackStackChangedListener? = null

    private var destinationChangeListener: NavController.OnDestinationChangedListener? = null

    private var wasPreviouslyShowingDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (savedInstanceState?.getSerializable(PENDING_REQUESTS) as? HashMap<Int, Bundle?>)
            ?.let { pendingRequests = it }
    }

    override fun onStart() {
        super.onStart()
        navHostFragmentIdCache = getNavHostFragmentId()
        attachDestinationChangeListener()
        attachBackStackChangeListener()
    }

    private fun attachDestinationChangeListener() {
        destinationChangeListener = NavController.OnDestinationChangedListener { _, destination, arguments ->
            checkForDialogStackChange(destination)
            arguments?.getInt(Constants.FRAGMENT_REQUEST_CODE, -1)
                ?.takeIf { it > -1 }
                ?.also { pendingRequests[it] = null }
        }.also {
            findNavController(navHostFragmentIdCache).addOnDestinationChangedListener(it)
        }
    }

    private fun checkForDialogStackChange(destination: NavDestination) {
        if (destination.navigatorName == DIALOG_NAVIGATOR) {
            wasPreviouslyShowingDialog = true
        } else if (destination.navigatorName == FRAGMENT_NAVIGATOR && wasPreviouslyShowingDialog) {
            wasPreviouslyShowingDialog = false
            backStackChangeListener?.onBackStackChanged()
        }
    }

    private fun attachBackStackChangeListener() {
        backStackChangeListener = FragmentManager.OnBackStackChangedListener {
            (supportFragmentManager.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.primaryNavigationFragment as? BundleFragment)
                ?.takeIf { it.pendingRequest > -1 }
                ?.takeIf { pendingRequests[it.pendingRequest] != null }
                ?.also {
                    it.onFragmentResult(it.pendingRequest, pendingRequests[it.pendingRequest]!!)
                }
                ?.also { pendingRequests.remove(it.pendingRequest) }
        }.also {
            supportFragmentManager.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.addOnBackStackChangedListener(it)
        }
    }

    internal fun setBundle(requestCode: Int, bundle: Bundle) {
        pendingRequests[requestCode] = bundle
    }

    fun navigate(navDirection: NavDirections, requestCode: Int = -1) {
        navigate(navDirection.actionId, navDirection.arguments, requestCode)
    }

    fun navigate(
        navDirection: NavDirections,
        navOptions: NavOptions?,
        requestCode: Int = -1
    ) {
        navigate(navDirection.actionId, navDirection.arguments, navOptions, null, requestCode)
    }

    fun navigate(
        navDirection: NavDirections,
        navigatorExtras: Navigator.Extras?,
        requestCode: Int = -1
    ) {
        navigate(navDirection.actionId, navDirection.arguments, null, navigatorExtras, requestCode)
    }

    fun navigate(@IdRes navDirection: Int, requestCode: Int) {
        navigate(navDirection, null, requestCode)
    }

    fun navigate(@IdRes navDirection: Int, bundle: Bundle?, requestCode: Int) {
        navigate(navDirection, bundle, null, requestCode)
    }

    fun navigate(
        @IdRes navDirection: Int,
        bundle: Bundle?,
        navOptions: NavOptions?,
        requestCode: Int
    ) {
        navigate(navDirection, bundle, navOptions, null, requestCode)
    }

    fun navigate(
        @IdRes navDirection: Int,
        bundle: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?,
        requestCode: Int
    ) {
        (supportFragmentManager.findFragmentById(getNavHostFragmentId())
            ?.childFragmentManager
            ?.primaryNavigationFragment as? BundleFragment)
            ?.navigate(navDirection, bundle, navOptions, navigatorExtras, requestCode)
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
            supportFragmentManager.findFragmentById(navHostFragmentIdCache)
                ?.childFragmentManager
                ?.removeOnBackStackChangedListener(it)
        }
    }

    private fun detachDestinationChangeListener() {
        destinationChangeListener?.let {
            findNavController(navHostFragmentIdCache).removeOnDestinationChangedListener(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PENDING_REQUESTS, pendingRequests)
    }

    @IdRes
    abstract fun getNavHostFragmentId(): Int

    companion object {
        private const val PENDING_REQUESTS = "pending_requests"
        private const val DIALOG_NAVIGATOR = "dialog"
        private const val FRAGMENT_NAVIGATOR = "fragment"
    }
}
