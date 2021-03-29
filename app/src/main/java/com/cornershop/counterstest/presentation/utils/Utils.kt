package com.cornershop.counterstest.presentation.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.closeKeyboard(view: View) {
    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.vibrateOnTouch() {
    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
}