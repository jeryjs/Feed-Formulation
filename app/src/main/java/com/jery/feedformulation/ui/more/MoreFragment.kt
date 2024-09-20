package com.jery.feedformulation.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentMoreBinding
import com.jery.feedformulation.ui.settings.SettingsFragment

class MoreFragment : Fragment() {
    private  lateinit var _binding: FragmentMoreBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMoreBinding.inflate(layoutInflater)

    }

    fun navigateToSettings(view: View) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .addToBackStack("app")
            .commit()
    }

}