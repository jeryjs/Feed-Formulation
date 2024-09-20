package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jery.feedformulation.databinding.FragmentResultBinding
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        tmrmakerViewModel.result.observe(viewLifecycleOwner) { result ->
            binding.resultTextView.text = result.joinToString(", ")
        }

        tmrmakerViewModel.calculateResult()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}