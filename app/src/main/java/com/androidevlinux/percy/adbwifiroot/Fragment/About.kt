package com.androidevlinux.percy.adbwifiroot.Fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidevlinux.percy.adbwifiroot.R
import kotlinx.android.synthetic.main.about_fragment.*




/**
 * Created by percy on 03/11/2017.
 */

class About : Fragment() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for fragment
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitle.text = activity?.resources?.getString(R.string.about)
        google_plus_link.setOnClickListener({
            openGPlus()
        })

        github_link.setOnClickListener({
            openGithub()
        })

        /*activity?.applicationContext?.let {
            TaskStackBuilder.create(it)
                    .addNextIntent(Intent(activity, MainActivity::class.java))
                    .addNextIntent(activity!!.intent)
                    .startActivities()
        }*/
    }

    private fun openGPlus() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(activity?.resources?.getString(R.string.google_plus_profile_link))))
        }
    }

    private fun openGithub() {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(activity?.resources?.getString(R.string.github_profile_link))
            startActivity(i)
        } catch (e: ActivityNotFoundException) {

        }
    }
}
