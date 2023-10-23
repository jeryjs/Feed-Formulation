package com.jery.feedformulation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jery.feedformulation.*
import com.jery.feedformulation.ui.activities.FeedsSelection


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btn = view.findViewById<Button>(R.id.start_button)
        btn.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            startActivity(intent)


            val dataDict = mapOf(
                "obj" to listOf(3.0, 3.0, 3.0, 3.0, 3.5, 3.0, 17.0, 38.0, 23.0, 23.0, 17.0, 21.0, 20.0, 17.0, 15.0, 15.0, 60.0, 5.0),
                "cp" to listOf(8.0, 7.0, 8.0, 3.5, 6.0, 3.0, 8.1, 42.0, 22.0, 32.0, 12.0, 16.0, 18.0, 22.0, 20.0, 50.0, 0.0, 0.0),
                "tdn" to listOf(52.0, 50.0, 60.0, 40.0, 42.0, 42.0, 79.2, 70.0, 70.0, 70.0, 70.0, 72.2, 45.0, 70.0, 65.0, 77.0, 0.0, 0.0),
                "ca" to listOf(0.38, 0.3, 0.53, 0.18, 0.15, 0.53, 0.53, 0.36, 0.2, 0.31, 1.067, 0.3, 0.3, 0.5, 0.5, 0.29, 32.0, 0.0),
                "ph" to listOf(0.36, 0.25, 0.14, 0.08, 0.09, 0.14, 0.41, 1.0, 0.9, 0.72, 0.093, 0.62, 0.62, 0.45, 0.4, 0.68, 15.0, 0.0),
                "per" to listOf(40.0, 10.0, 40.0, 20.0, 10.0, 40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 0.1, 0.1)
            )
        }
        btn.performClick()
        return view
    }
}