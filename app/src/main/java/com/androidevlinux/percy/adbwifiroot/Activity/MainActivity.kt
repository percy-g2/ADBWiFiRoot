package com.androidevlinux.percy.adbwifiroot.Activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.androidevlinux.percy.adbwifiroot.Fragment.About
import com.androidevlinux.percy.adbwifiroot.Fragment.NoRootAdb
import com.androidevlinux.percy.adbwifiroot.Fragment.RootAdb
import com.androidevlinux.percy.adbwifiroot.Fragment.Settings
import com.androidevlinux.percy.adbwifiroot.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var fragment: Fragment? = null
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
        displaySelectedScreen(R.id.nav_adb_root)
        txtTitle.text = this.resources?.getString(R.string.app_name)
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
        displaySelectedScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySelectedScreen(itemId: Int) {
        when (itemId) {
            R.id.nav_adb_root -> {
                fragment = RootAdb()
            }
            R.id.nav_adb_no_root -> {
                fragment = NoRootAdb()
            }
            R.id.nav_settings -> {
                fragment = Settings()
            }
            R.id.nav_about -> {
                fragment = About()
            }
        }

        //replacing the fragment
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container, fragment)
            ft.addToBackStack(null).commit()
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
