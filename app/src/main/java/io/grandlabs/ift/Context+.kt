package io.grandlabs.ift

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.widget.Toast

inline val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

inline val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.showProgressDialog(text: String): ProgressDialog {
    val progress = ProgressDialog(this)
    progress.setTitle(text)
    progress.setCancelable(false)
    progress.show()
    return progress
}

fun Context.shortToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}