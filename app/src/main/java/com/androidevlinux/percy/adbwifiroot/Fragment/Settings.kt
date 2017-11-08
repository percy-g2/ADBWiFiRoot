package com.androidevlinux.percy.adbwifiroot.Fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.View
import com.androidevlinux.percy.adbwifiroot.R
import java.io.IOException
import java.io.OutputStreamWriter


/**
 * Created by percy on 03/11/2017.
 */

class Settings : PreferenceFragmentCompat() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    private val reboot_pref by lazy { findPreference(reboot_pref_keys)  }
    private var reboot_pref_keys = "reboot_pref_keys"
    private val power_off_pref by lazy { findPreference(power_off_pref_keys)  }
    private var power_off_pref_keys = "power_off_pref_keys"
    private val soft_reboot_pref by lazy { findPreference(soft_reboot_pref_keys)  }
    private var soft_reboot_pref_keys = "soft_reboot_pref_keys"
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        txtTitle.text = activity?.resources?.getString(R.string.action_settings)
        try {
            PreferenceManager.getDefaultSharedPreferences(activity)
        } catch (e: Exception) {
            Log.i("test", e.message)
        }

        reboot_pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            rebootDialog()
            true
        }

        soft_reboot_pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            softRebootDialog()
            true
        }

        power_off_pref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            powerOffDialog()
            true
        }
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
                if (os != null) os.close()
                if (process != null) {
                    process.destroy()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return true
    }
}
