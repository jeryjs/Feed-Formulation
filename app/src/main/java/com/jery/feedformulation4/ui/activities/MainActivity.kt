package com.jery.feedformulation4.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.jery.feedformulation4.databinding.ActivityMainBinding
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var B: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = ActivityMainBinding.inflate(layoutInflater)
        setContentView(B.root)

        B.button.setOnClickListener {
            val intent = Intent(this, FeedsListActivity::class.java)
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", true)
            startActivity(intent)
        }
        B.button2.setOnClickListener {
            val intent = Intent(this, FeedsListActivity::class.java)
            intent.putExtra("IS_SELECT_FEEDS_ENABLED", false)
            startActivity(intent)
        }

        B.sprBW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        B.sprMY.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        B.sprMF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }
        B.sprPr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {calc()}
        }

//        startActivity(Intent(this, FeedsListActivity()::class.java))
    }
    @SuppressLint("SetTextI18n")
    fun calc() {
        val bw = B.sprBW.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val my = B.sprMY.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val mf = B.sprMF.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toDouble()
        val pr = B.sprPr.selectedItem.toString().replace(Regex("""(\d+).+"""), "$1").toIntOrNull() ?: 0

        B.linearLayout2.visibility = View.VISIBLE

        val (dm, cp, tdn) = calculateNutrients("cow", bw, my, mf, pr)
        B.txtDM.text = "DM (KG):        ${String.format("%.2f", dm)}"
        B.txtCP.text = "CP  (g):        ${String.format("%.2f", cp)}"
        B.txtTDN.text= "TDN (g):        ${String.format("%.2f", tdn)}"
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