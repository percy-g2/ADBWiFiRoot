package com.androidevlinux.percy.adbwifiroot.Fragment

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidevlinux.percy.adbwifiroot.Activity.MainActivity
import com.androidevlinux.percy.adbwifiroot.R
import kotlinx.android.synthetic.main.no_root_adb_fragment.*




/**
 * Created by percy on 04/11/2017.
 */

class NoRootAdb : Fragment() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for fragment
        return inflater.inflate(R.layout.no_root_adb_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitle.text = activity?.resources?.getString(R.string.adb_no_root)
        turn_on_adb_wifi.setOnCheckedChangeListener{ _, isChecked ->
            if (checkWiFi()) {
                if (isChecked) {
                    txt_ip.text = activity?.resources?.getText(R.string.no_root_description).toString().plus(" " + getIP() + ":5555")
                    notify(getIP()+ ":5555")
                } else{
                    txt_ip.text = activity?.resources?.getText(R.string.turn_on)
                    notify(resources.getText(R.string.adb_connect_0).toString())
                }
            } else{
                notify(activity?.resources?.getText(R.string.no_wifi_connected).toString())
                txt_ip.text = activity?.resources?.getText(R.string.no_wifi_connected)
                turn_on_adb_wifi.isChecked = false
                Snackbar.make(turn_on_adb_wifi, "Connect To WiFi First", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun notify(toString: String) {
        val builder = activity?.let { NotificationCompat.Builder(it, "default") }
        val notificationIntent = Intent(activity, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0)
        builder?.setSmallIcon(R.drawable.ic_stat_adb)
                ?.setContentTitle(toString)
                ?.setContentIntent(pendingIntent)
        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(R.drawable.ic_stat_adb, builder?.build())
    }

    private fun checkWiFi(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //Snackbar.make(turn_on_adb_wifi, activeNetwork.typeName, Snackbar.LENGTH_SHORT).show()
                return true
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //Snackbar.make(turn_on_adb_wifi, activeNetwork.typeName, Snackbar.LENGTH_SHORT).show()
                return false
            }
        } else {
            // not connected to the internet
            return false
        }
        return false
    }
    private fun getIP(): String {
        val wifiMgr = activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMgr.connectionInfo
        val ip = wifiInfo.ipAddress
        return String.format("%d.%d.%d.%d", ip and 0xff, ip shr 8 and 0xff, ip shr 16 and 0xff, ip shr 24 and 0xff)
    }
}
