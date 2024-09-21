package com.jery.feedformulation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentHomeBinding
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnTmrmaker.setOnClickListener {
            findNavController().navigate(R.id.nav_tmrmaker)
        }

        var currentLanguage = Locale.getDefault().language

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = resources.getStringArray(R.array.language_values)[position]
                if (selectedLanguage != currentLanguage) {
                    currentLanguage = selectedLanguage
                    val locale = Locale(selectedLanguage)
                    Locale.setDefault(locale)
                    val config = resources.configuration
                    config.setLocale(locale)
                    resources.updateConfiguration(config, resources.displayMetrics)
                    try {
                        activity?.recreate()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}