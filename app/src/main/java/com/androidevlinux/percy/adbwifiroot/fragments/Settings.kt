package com.androidevlinux.percy.adbwifiroot.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.androidevlinux.percy.adbwifiroot.R
import java.io.IOException
import java.io.OutputStreamWriter


/**
 * Created by percy on 03/11/2017.
 */

class Settings : PreferenceFragmentCompat() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    private val rebootPref by lazy { findPreference(rebootPrefKeys) }
    private var rebootPrefKeys = "reboot_pref_keys"
    private val powerOffPref by lazy { findPreference(powerOffPrefKeys) }
    private var powerOffPrefKeys = "power_off_pref_keys"
    private val softRebootPref by lazy { findPreference(softRebootPrefKeys) }
    private var softRebootPrefKeys = "soft_reboot_pref_keys"
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        try {
            PreferenceManager.getDefaultSharedPreferences(activity)
        } catch (e: Exception) {
            Log.i("test", e.message)
        }

        rebootPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            rebootDialog()
            true
        }

        softRebootPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            softRebootDialog()
            true
        }

        powerOffPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            powerOffDialog()
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitle.text = activity?.resources?.getString(R.string.action_settings)
    }

    private fun rebootDialog() {
        val builder = activity?.let { AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle) }
        builder?.setTitle("Confirm")
        builder?.setCancelable(true)
        builder?.setMessage(resources.getString(R.string.reboot_alert))
        builder?.setPositiveButton("Yes") { dialog, _ ->
            reboot("reboot")
            dialog.dismiss()
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.show()
    }

    private fun powerOffDialog() {
        val builder = activity?.let { AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle) }
        builder?.setTitle("Confirm")
        builder?.setCancelable(true)
        builder?.setMessage(resources.getString(R.string.power_off_alert))
        builder?.setPositiveButton("Yes") { dialog, _ ->
            reboot("power off")
            dialog.dismiss()
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.show()
    }

    private fun softRebootDialog() {
        val builder = activity?.let { AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle) }
        builder?.setTitle("Confirm")
        builder?.setCancelable(true)
        builder?.setMessage(resources.getString(R.string.soft_reboot_alert))
        builder?.setPositiveButton("Yes") { dialog, _ ->
            reboot("soft reboot")
            dialog.dismiss()
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.show()
    }

    private fun reboot(s: String): Boolean {
        var process: Process? = null
        var os: OutputStreamWriter? = null

        try {
            process = Runtime.getRuntime().exec("su")
            os = OutputStreamWriter(process!!.outputStream)
            when {
                s.equals("soft reboot", true) -> {
                    os.write("setprop ctl.restart surfaceflinger\n")
                    os.write("setprop ctl.restart zygote\n")
                    os.write("exit\n")
                }
                s.equals("reboot", true) -> {
                    os.write("reboot\n")
                    os.write("exit\n")
                }
                s.equals("power off", true) -> {
                    os.write("reboot -p\n")
                    os.write("exit\n")
                }
            }
            os.flush()
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                process?.destroy()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return true
    }
}
