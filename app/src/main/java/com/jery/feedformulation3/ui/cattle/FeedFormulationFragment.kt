package com.jery.feedformulation3.ui.cattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jery.feedformulation3.R

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFormulationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFormulationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_formulation, container, false)
    }
}