package com.phelat.navigationresultsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phelat.navigationresult.BundleFragment
import kotlinx.android.synthetic.main.intro_fragment.*

class IntroFragment : BundleFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.intro_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            navigate(IntroFragmentDirections.introToLogin(LOGIN_RESULT_CODE))
        }
        registerButton.setOnClickListener {
            navigate(IntroFragmentDirections.introToRegister(REGISTER_RESULT_CODE))
        }
    }

    override fun onFragmentResult(resultCode: Int, bundle: Bundle) {
        when (resultCode) {
            LOGIN_RESULT_CODE -> {
                introPage.visibility = View.INVISIBLE
                userPanelPage.visibility = View.VISIBLE
                panelMessage.setText(R.string.general_login_success_message)
            }
            REGISTER_RESULT_CODE -> {
                introPage.visibility = View.INVISIBLE
                userPanelPage.visibility = View.VISIBLE
                panelMessage.setText(R.string.general_register_success_message)
            }
        }
    }

    companion object {
        const val LOGIN_RESULT_CODE = 1
        const val REGISTER_RESULT_CODE = 2
    }

}
