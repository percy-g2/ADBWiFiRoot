package com.androidevlinux.percy.adbwifiroot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.androidevlinux.percy.adbwifiroot.BuildConfig
import com.androidevlinux.percy.adbwifiroot.R
import com.vansuita.materialabout.builder.AboutBuilder

/**
 * Created by percy on 03/11/2017.
 */

class About : Fragment() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val builder = AboutBuilder.with(activity)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.mipmap.ic_photo)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Prashant Gahlot")
                .setSubTitle("Android Developer")
                .addLinkedInLink("prashant-gahlot-37914988")
                .setBrief("I love everything related to open source software and block chain ;)")
                .setLinksColumnsCount(4)
                .addGitHubLink("percy-g2")
                .addEmailLink("contact.prashantgahlot@gmail.com")
                .addWebsiteLink("http://androdevlinux.in/")
                .addFiveStarsAction()
                .setAppTitle("Version : " + BuildConfig.VERSION_NAME)
                .addShareAction(R.string.app_name)
                .setActionsColumnsCount(2)
                .setWrapScrollView(true)
                .setShowAsCard(true)
        return builder.build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitle.text = activity?.resources?.getString(R.string.about)

    }
}
