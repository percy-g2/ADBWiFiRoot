package com.androidevlinux.percy.adbwifiroot.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.androidevlinux.percy.adbwifiroot.R
import com.androidevlinux.percy.adbwifiroot.fragments.About
import com.androidevlinux.percy.adbwifiroot.fragments.NoRootAdb
import com.androidevlinux.percy.adbwifiroot.fragments.RootAdb
import com.androidevlinux.percy.adbwifiroot.fragments.Settings
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var fragment: Fragment? = null
    private var doubleBackToExitPressedOnce = false
    private val actionSettings = "com.androidevlinux.percy.adbwifiroot.Activity.SETTINGS"
    private val actionAbout = "com.androidevlinux.percy.adbwifiroot.Activity.ABOUT"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager.addOnBackStackChangedListener {
            val fm = supportFragmentManager
            var stackName = ""
            for (entry in 0 until fm.backStackEntryCount) {
                stackName = fm.getBackStackEntryAt(entry).name.toString()
            }
            var navItemId = -1
            try {
                navItemId = Integer.parseInt(stackName)
            } catch (nfe: NumberFormatException) {
                nfe.stackTrace
            }

            if (navItemId > 0) {
                nav_view.setCheckedItem(navItemId)
            } else {
                Log.e("MainActivity", "Couldn't find the id")
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemIconTintList = null
        when {
            actionSettings == intent.action -> // Invoked via the manifest shortcut.
                displaySelectedScreen(R.id.nav_settings)
            actionAbout == intent.action -> // Invoked via the manifest shortcut.
                displaySelectedScreen(R.id.nav_about)
            else -> displaySelectedScreen(R.id.nav_adb_root)
        }
        txtTitle.text = this.resources?.getString(R.string.app_name)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            if (supportFragmentManager.backStackEntryCount == 1) {
                if (doubleBackToExitPressedOnce) {
                    finish()
                } else {
                    Toast.makeText(this, "Press BACK again to exit!", Toast.LENGTH_SHORT).show()
                }
                doubleBackToExitPressedOnce = true
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            } else {
                super.onBackPressed()
            }
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
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .setCustomAnimations(
                            android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out
                    )
                    .replace(R.id.container, fragment!!)
                    .addToBackStack(itemId.toString())
                    .commit()
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
