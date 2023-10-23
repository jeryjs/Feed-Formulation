package com.jery.feedformulation3.ui.main

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jery.feedformulation3.R

/**
 * A [Fragment] for displaying the list of feeds.
 * Use the [FeedsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feeds, container, false)
        val toast: Toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)

        // Set the placeholder image to display a toast message when clicked
        view.findViewById<ImageView>(R.id.placeholder).setOnClickListener {
            toast.cancel()
            toast.setText("PLACEHOLDER: Displays list of feeds")
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }

        //
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            toast.cancel()
            toast.setText("TODO: Start activity to add new feed")
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }
        return view
    }

}