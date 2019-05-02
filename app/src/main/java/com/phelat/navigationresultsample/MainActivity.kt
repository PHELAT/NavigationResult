package com.phelat.navigationresultsample

import android.os.Bundle
import com.phelat.navigationresult.FragmentResultActivity

class MainActivity : FragmentResultActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun getNavHostFragmentId(): Int = R.id.nav_host_fragment

}
