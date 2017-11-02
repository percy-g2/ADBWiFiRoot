package com.androidevlinux.percy.adbwifiroot

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.format.Formatter
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.io.OutputStreamWriter




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemIconTintList = null

        turn_on_adb_wifi.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                enableWifiAdb(!checkAdb())
                changeState()
                Snackbar.make(turn_on_adb_wifi, "ADB WiFi Turned On", Snackbar.LENGTH_SHORT).show()
            } else {
                enableWifiAdb(!checkAdb())
                changeState()
                Snackbar.make(turn_on_adb_wifi, "ADB WiFi Turned Off", Snackbar.LENGTH_SHORT).show()
            }
        }
        changeState()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_settings -> {
                Snackbar.make(turn_on_adb_wifi, "TODO", Snackbar.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {
                Snackbar.make(turn_on_adb_wifi, "TODO", Snackbar.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun changeState() {
        //val enabled = checkAdb()
        if (turn_on_adb_wifi.isChecked) {
            txt_ip.text =  resources.getText(R.string.adb_connect).toString().plus(" " + getIP())
        } else {
            txt_ip.text = resources.getText(R.string.adb_connect_0)
        }
        Notify(txt_ip.text.toString())
    }

    private fun Notify(toString: String) {


        val builder = Notification.Builder(this@MainActivity)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setSmallIcon(R.drawable.ic_stat_adb)
                .setContentTitle(toString)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = builder.notification
        notificationManager.notify(R.drawable.ic_stat_adb, notification)
    }

    fun enableWifiAdb(enable: Boolean): Boolean {
        var process: Process? = null
        var os: OutputStreamWriter? = null

        try {
            process = Runtime.getRuntime().exec("su")
            os = OutputStreamWriter(process!!.outputStream)
            os.write("setprop service.adb.tcp.port " + (if (enable) "5555" else "-1") + "\n")
            os.write("stop adbd\n")
            os.write("start adbd\n")
            os.write("exit\n")
            os.flush()
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
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

    fun checkAdb(): Boolean {
        var process: Process? = null
        var `in`: InputStreamReader? = null
        try {
            process = Runtime.getRuntime().exec("getprop service.adb.tcp.port")
            `in` = InputStreamReader(process!!.inputStream)

            val input = LineNumberReader(`in`)

            process.waitFor()

            val line = input.readLine()
            if (line != null && line.trim({ it <= ' ' }) != "-1") {
                return true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                    process!!.destroy()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return false
    }

    @SuppressLint("WifiManagerLeak", "MissingPermission")
    fun getIP(): String {
        val wm = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        return ip
    }
}
