package com.phelat.navigationresultsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener(::onLoginButtonClicked)
    }

    private fun onLoginButtonClicked(view: View) {
        if (isUsernameAndPasswordValid()) {
            navigateUp(IntroFragment.LOGIN_REQUEST_CODE, Bundle().apply {
                putBoolean(IS_LOGIN_SUCCESSFUL, true)
            })
        } else {
            Snackbar.make(
                view,
                R.string.general_wrong_login_credentials_message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun isUsernameAndPasswordValid(): Boolean {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()
        return username == "admin" && password == "1234"
    }

    companion object {
        const val IS_LOGIN_SUCCESSFUL = "isLoginSuccessful"
    }

}
