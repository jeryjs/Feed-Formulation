package com.jery.feedformulation.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.jery.feedformulation.data.Nutrients
import com.jery.feedformulation.databinding.FragmentInfoBinding
import com.jery.feedformulation.ui.activities.FeedsSelection
import kotlin.math.pow

/**
 * A [Fragment] subclass to display the examples.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    private lateinit var _b: FragmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentInfoBinding.inflate(inflater, container, false)

        _b.button.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", true)
            startActivity(intent)
        }
        _b.button2.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", false)
            startActivity(intent)
        }

        _b.sprBW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprMY.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprMF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprPr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        return _b.root
    }
    @SuppressLint("SetTextI18n")
    fun calc() {
        val bw = _b.sprBW.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val my = _b.sprMY.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val mf = _b.sprMF.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val pr = _b.sprPr.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toIntOrNull() ?: 0

        _b.linearLayout2.visibility = View.VISIBLE

        val nutrients = Nutrients(bw, my, mf, pr)
        Nutrients.setInstance(nutrients)
        val (dm, cp, tdn) = nutrients.calculateNutrients()

        _b.txtDM.text = "DM (KG):        ${String.format("%.2f", dm)}"
        _b.txtCP.text = "CP  (g):        ${String.format("%.2f", cp)}"
        _b.txtTDN.text= "TDN (g):        ${String.format("%.2f", tdn)}"
    }
}