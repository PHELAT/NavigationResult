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
            navigate(IntroFragmentDirections.introToLogin(LOGIN_REQUEST_CODE))
        }
        registerButton.setOnClickListener {
            navigate(IntroFragmentDirections.introToRegister(), REGISTER_REQUEST_CODE)
        }
        changeNameButton.setOnClickListener {
            navigate(IntroFragmentDirections.introToChangeName(), CHANGE_NAME_REQUEST_CODE)
        }
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                introPage.visibility = View.INVISIBLE
                userPanelPage.visibility = View.VISIBLE
                panelMessage.setText(R.string.general_login_success_message)
            }
            REGISTER_REQUEST_CODE -> {
                introPage.visibility = View.INVISIBLE
                userPanelPage.visibility = View.VISIBLE
                panelMessage.setText(R.string.general_register_success_message)
            }
            CHANGE_NAME_REQUEST_CODE -> {
                introPage.visibility = View.INVISIBLE
                userPanelPage.visibility = View.VISIBLE
                panelMessage.text = getString(
                    R.string.general_name_change_success_message,
                    bundle.getString(ChangeNameDialog.NAME_KEY)
                )
            }
        }
    }

    companion object {
        const val LOGIN_REQUEST_CODE = 1
        const val REGISTER_REQUEST_CODE = 2
        const val CHANGE_NAME_REQUEST_CODE = 3
    }

}
