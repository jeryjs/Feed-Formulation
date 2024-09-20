package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentResultBinding
import com.jery.feedformulation.viewmodel.TmrmakerViewModel
import com.jery.feedformulation.model.SimplexResult
import com.jery.feedformulation.utils.Simplex

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]

        (context as AppCompatActivity).supportActionBar!!.title =
            getString(R.string.feeds_to_be_included_in_ration_for, tmrmakerViewModel.selectedCattle)

        val s = Simplex()
        s.solve(tmrmakerViewModel)

        displayResult()

        return binding.root
    }

    private fun displayResult() {
        val simplexResult = tmrmakerViewModel.result.value!!
        val stringBuilder = StringBuilder()
        stringBuilder.append("Total DM: ${"%.2f".format(simplexResult.totalDm)}\n")
        stringBuilder.append("Total CP: ${"%.2f".format(simplexResult.totalCp)}\n")
        stringBuilder.append("Total TDN: ${"%.2f".format(simplexResult.totalTdn)}\n\n")
        stringBuilder.append("Feed Weights:\n")
        simplexResult.feedWeights.filter { it.second > 0.0 }.forEach { (feedName, weight) ->
            stringBuilder.append("$feedName: ${"%.2f".format(weight)} Kg\n")
        }
        binding.tvLog.text = stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}