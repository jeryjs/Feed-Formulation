package com.jery.feedformulation6.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.jery.feedformulation6.databinding.FragmentInfoBinding
import com.jery.feedformulation6.ui.activities.FeedsSelection
import kotlin.math.pow

/**
 * A [Fragment] subclass to display the examples.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", true)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", false)
            startActivity(intent)
        }

        binding.sprBW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        binding.sprMY.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        binding.sprMF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        binding.sprPr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    fun calc() {
        val bw = binding.sprBW.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val my = binding.sprMY.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val mf = binding.sprMF.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val pr = binding.sprPr.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toIntOrNull() ?: 0

        binding.linearLayout2.visibility = View.VISIBLE

        val (dm, cp, tdn) = calculateNutrients("cow", bw, my, mf, pr)
        binding.txtDM.text = "DM (KG):        ${String.format("%.2f", dm)}"
        binding.txtCP.text = "CP  (g):        ${String.format("%.2f", cp)}"
        binding.txtTDN.text= "TDN (g):        ${String.format("%.2f", tdn)}"
    }

    private fun calculateNutrients(cattle:String, bw: Double, my: Double, mf: Double, pr: Int): List<Double> {
        var dm = 0.0; var cp = 0.0; var tdn = 0.0

        when (cattle) {
            "cow" -> {
                dm = 0.0216 * bw + my * (0.063 * mf + 0.259) + (0.14 * 0 + 0.01)
                if (pr == 0) {
                    cp = 4.87 * bw.pow(0.75) + 96 * my
                    tdn = 1e3 * (0.038 * bw.pow(0.75) - 0.093) + my * (42 * mf + 162)
                } else {
                    cp = 4.87 * bw.pow(0.75) + 96 * my + (47 * pr - 113)
                    tdn = 1e3 * (0.038 * bw.pow(0.75) - 0.093) + (my * (42 * mf + 162) + (100 * pr + 40))
                }
            }
            "buffalo" -> {
                dm = 0.0216 * bw + my * (0.0632 * mf + 0.2946) + (0.17 * pr - 0.17)
                cp = 4.87 * bw.pow(0.75) + 124 * my + (56.7 * pr - 194.2)
                tdn = 1e3 * (0.038 * bw.pow(0.75) - 0.093 + my * (0.2 + 0.04 * mf) + (0.1 + 0.1 * pr))
            }
        }
        return listOf(dm, cp, tdn)
    }
}