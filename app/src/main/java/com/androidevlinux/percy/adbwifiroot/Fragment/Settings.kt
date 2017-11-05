package com.androidevlinux.percy.adbwifiroot.Fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.View
import com.androidevlinux.percy.adbwifiroot.R

/**
 * Created by percy on 03/11/2017.
 */

class Settings : PreferenceFragmentCompat() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        txtTitle.text = activity?.resources?.getString(R.string.action_settings)
        try {
            PreferenceManager.getDefaultSharedPreferences(activity)
        } catch (e: Exception) {
            Log.i("test", e.message)
        }
    }
}
