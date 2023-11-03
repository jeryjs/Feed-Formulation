package com.jery.feedformulation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jery.feedformulation.*
import com.jery.feedformulation.databinding.FragmentHomeBinding
import com.jery.feedformulation.ui.activities.Formulation
import com.jery.feedformulation.utils.Constants as c


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
    ): View {
        val _b = FragmentHomeBinding.inflate(inflater, container, false)
        _b.cowButton.setOnClickListener {
            val intent = Intent(activity, Formulation::class.java).setAction(c.CATTLE_COW)
            startActivity(intent)
        }
        _b.buffaloButton.setOnClickListener {
            val intent = Intent(activity, Formulation::class.java).setAction(c.CATTLE_BUFFALO)
            startActivity(intent)
        }
//        btn.performClick()
        return _b.root
    }
}