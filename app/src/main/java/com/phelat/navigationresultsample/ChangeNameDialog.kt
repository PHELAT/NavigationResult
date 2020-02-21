package com.phelat.navigationresultsample

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.phelat.navigationresult.navigateUp

class ChangeNameDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.change_name_dialog_fragment, null, false)
        val nameInput = dialogView.findViewById<EditText>(R.id.nameInput)
        return AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.general_ok_text) { _, _ ->
                navigateUp(IntroFragment.CHANGE_NAME_REQUEST_CODE, Bundle().apply {
                    putString(NAME_KEY, nameInput.text.toString())
                })
            }
            .setNegativeButton(R.string.general_cancel_text) { dialog, _ ->
                dialog.cancel()
            }
            .setView(dialogView)
            .create()
    }

    companion object {
        const val NAME_KEY = "NAME_KEY"
    }
}