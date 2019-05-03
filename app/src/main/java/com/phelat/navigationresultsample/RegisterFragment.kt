package com.phelat.navigationresultsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.register_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton.setOnClickListener {
            navigateUp(IntroFragment.REGISTER_RESULT_CODE, Bundle().apply {
                putBoolean(IS_REGISTER_SUCCESSFUL, true)
            })
        }
    }

    companion object {
        const val IS_REGISTER_SUCCESSFUL = "isRegisterSuccessful"
    }

}
