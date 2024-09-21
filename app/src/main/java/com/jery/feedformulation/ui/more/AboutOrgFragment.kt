package com.jery.feedformulation.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jery.feedformulation.R

class AboutOrgFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_org, container, false).apply {
            findViewById<TextView>(R.id.tvIcarNianpWebsite).setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://nianp.res.in/")))
            }
        }
    }
}