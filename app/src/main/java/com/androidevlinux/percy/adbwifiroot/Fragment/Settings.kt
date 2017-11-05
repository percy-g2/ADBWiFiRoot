package com.androidevlinux.percy.adbwifiroot.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidevlinux.percy.adbwifiroot.R

/**
 * Created by percy on 03/11/2017.
 */

class Settings : Fragment() {
    private val txtTitle by lazy { activity!!.findViewById<View>(R.id.txtTitle) as AppCompatTextView }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for fragment
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTitle.text = activity?.resources?.getString(R.string.action_settings)
    }
}
