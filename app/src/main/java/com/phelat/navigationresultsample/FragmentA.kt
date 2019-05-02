package com.phelat.navigationresultsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.phelat.navigationresult.BundleFragment
import kotlinx.android.synthetic.main.fragment_a.*

class FragmentA : BundleFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_a, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.a_to_b))
    }

    override fun onFragmentResult(resultCode: Int, bundle: Bundle) {
        println(bundle.getBoolean(FragmentB.IS_LOGIN_SUCCESSFUL, false))
    }

}
