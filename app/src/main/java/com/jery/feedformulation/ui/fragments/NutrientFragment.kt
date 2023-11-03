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
import com.jery.feedformulation.databinding.FragmentNutrientBinding
import com.jery.feedformulation.ui.activities.FeedsSelection
import com.jery.feedformulation.ui.activities.MainActivity
import com.jery.feedformulation.utils.Constants as c

/**
 * A [Fragment] subclass to display the examples.
 * Use the [NutrientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NutrientFragment(private val cattle: String = c.CATTLE_COW) : Fragment() {
    private lateinit var _b: FragmentNutrientBinding
    private var nutrients = Nutrients.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentNutrientBinding.inflate(inflater, container, false)

        nutrients = Nutrients.getInstance()

        if (activity is MainActivity) {
            _b.button.visibility = View.VISIBLE
            _b.button2.visibility = View.VISIBLE
            _b.textView2.visibility = View.VISIBLE
        } else {
            _b.button.visibility = View.GONE
            _b.button2.visibility = View.GONE
            _b.textView2.visibility = View.GONE
        }
        _b.button.setOnClickListener {
            nutrients.calculateNutrients()
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            startActivity(intent)
        }
        _b.button2.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java)
            startActivity(intent)
        }

        _b.sprBW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprMY.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprMF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {calc()}
        }
        _b.sprPr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {calc()}
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

        nutrients = Nutrients(bw, my, mf, pr)
        nutrients.type = cattle
        Nutrients.setInstance(nutrients)
        val (dm, cp, tdn) = nutrients.calculateNutrients()

        _b.txtDM.text = "DM (KG):        ${String.format("%.2f", dm)}   to   ${String.format("%.2f", dm + 1.0)}"
        _b.txtCP.text = "CP  (g):        ${String.format("%.2f", cp)}   to   ${String.format("%.2f", cp * 1.1)}"
        _b.txtTDN.text= "TDN (g):        ${String.format("%.2f", tdn)}   to   ${String.format("%.2f", tdn * 1.1)}"
    }
}