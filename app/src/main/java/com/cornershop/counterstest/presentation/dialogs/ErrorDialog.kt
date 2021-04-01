package com.cornershop.counterstest.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cornershop.counterstest.R

class ErrorDialog(private val errorMessage: String?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.error_creating_counter_title)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.ok) { _, _ ->
                dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}