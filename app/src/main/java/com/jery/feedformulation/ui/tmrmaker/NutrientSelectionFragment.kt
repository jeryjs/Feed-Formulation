package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentNutrientSelectionBinding
import com.jery.feedformulation.model.Nutrients
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class NutrientSelectionFragment : Fragment() {

    private var _binding: FragmentNutrientSelectionBinding? = null
    private val b get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        _binding = FragmentNutrientSelectionBinding.inflate(inflater, container, false)

        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateNutrients()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        b.sprBW.onItemSelectedListener = itemSelectedListener
        b.sprMY.onItemSelectedListener = itemSelectedListener
        b.sprMF.onItemSelectedListener = itemSelectedListener
        b.sprPr.onItemSelectedListener = itemSelectedListener

        return b.root
    }

    private fun updateNutrients() {
        val bw = b.sprBW.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val my = b.sprMY.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val mf = b.sprMF.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val pr = b.sprPr.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toIntOrNull() ?: 0
        val nutrients = Nutrients(bw, my, mf, pr)
        nutrients.calculateNutrients()
        tmrmakerViewModel.selectNutrients(nutrients)
        displayNutrientValues(nutrients)
    }

    private fun displayNutrientValues(nutrients: Nutrients) {
        b.txtDM.text = getString(R.string.dm_value, nutrients.dm)
        b.txtCP.text = getString(R.string.cp_value, nutrients.cp)
        b.txtTDN.text = getString(R.string.tdn_value, nutrients.tdn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}