package com.jery.feedformulation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.jery.feedformulation.*
import com.jery.feedformulation.ui.activities.FeedsSelection
import com.jery.feedformulation.ui.activities.Formulation


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
//            val fragment = NutrientFragment()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, fragment)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .addToBackStack(null)
//                .commit()
            val intent = Intent(activity, Formulation::class.java)
            startActivity(intent)
        }
//        btn.performClick()
        return view
    }
}