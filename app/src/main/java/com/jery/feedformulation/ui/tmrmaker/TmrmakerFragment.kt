package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentTmrmakerBinding
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class TmrmakerFragment : Fragment() {

    private var _binding: FragmentTmrmakerBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        _binding = FragmentTmrmakerBinding.inflate(inflater, container, false)

        // Initialize with the CattleSelectionFragment
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                replace(R.id.fragment_container, CattleSelectionFragment())
            }
        }

        binding.fabNext.setOnClickListener {
            navigateToNextFragment()
        }

        childFragmentManager.addOnBackStackChangedListener {
            updateFabVisibility()
        }

        return binding.root
    }

    private fun navigateToNextFragment() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.fragment_container)
        val nextFragment = when (currentFragment) {
            is CattleSelectionFragment -> NutrientSelectionFragment()
            is NutrientSelectionFragment -> FeedSelectionFragment()
            is FeedSelectionFragment -> ResultFragment()
            else -> null
        }

        nextFragment?.let {
            childFragmentManager.commit {
                setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                replace(R.id.fragment_container, it)
                addToBackStack(null)
            }
        }
    }

    private fun updateFabVisibility() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.fragment_container)
        binding.fabNext.visibility = if (currentFragment is CattleSelectionFragment) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}