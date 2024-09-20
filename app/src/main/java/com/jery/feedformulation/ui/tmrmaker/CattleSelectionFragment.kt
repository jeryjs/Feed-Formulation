package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentCattleSelectionBinding
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class CattleSelectionFragment : Fragment() {

    private var _binding: FragmentCattleSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        _binding = FragmentCattleSelectionBinding.inflate(inflater, container, false)

        binding.buttonCow.setOnClickListener {
            tmrmakerViewModel.selectCattle("Cow")
            navigateToNextFragment()
        }

        binding.buttonBuffalo.setOnClickListener {
            tmrmakerViewModel.selectCattle("Buffalo")
            navigateToNextFragment()
        }

        return binding.root
    }

    private fun navigateToNextFragment() {
        parentFragmentManager.commit {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            replace(R.id.fragment_container, NutrientSelectionFragment())
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}