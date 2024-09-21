package com.jery.feedformulation.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        binding.tvExampleTmr.setOnClickListener { findNavController().navigate(R.id.nav_example_tmr) }
        binding.tvExampleCm.setOnClickListener { findNavController().navigate(R.id.nav_example_cm) }
        binding.tvSettings.setOnClickListener { findNavController().navigate(R.id.nav_settings) }
        binding.tvAbout.setOnClickListener { findNavController().navigate(R.id.nav_about) }
        binding.tvAboutOrganization.setOnClickListener { findNavController().navigate(R.id.nav_about_org) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
