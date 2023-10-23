package com.jery.feedformulation7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TMR_Data : AppCompatActivity() {
    private var spinner: Spinner? = null
    private val spinnerID = intArrayOf(R.id.BW_values, R.id.MY_values, R.id.MF_values, R.id.P_values)
    private val spinnerArray = intArrayOf(R.array.BW_values, R.array.MY_values, R.array.MF_values, R.array.P_values)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Nutrients.type == 0) setTitle(R.string.cow_title) else setTitle(R.string.buffalo_title)
        setContentView(R.layout.activity_tmr_data)

        for (i in spinnerID.indices) {
            spinner = findViewById<View>(spinnerID[i]) as Spinner
            val spinnerStrings = resources.getStringArray(spinnerArray[i])
            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerStrings
            )
            spinner!!.adapter = spinnerAdapter
            spinner!!.setSelection(1, true)
        }
    }

    fun CalculateNutrients(v: View?) {
        val nutrients: Nutrients
        val spinnerValues = IntArray(4)
        for (i in spinnerID.indices) {
            spinner = findViewById<View>(spinnerID[i]) as Spinner
            val text = spinner!!.selectedItem.toString()
            if (text == "Select Body Weight" || text == "Select Milk Yield" || text == "Select Milk Fat" || text == "Select Age" || text == "Select status of pregnancy") {
                Toast.makeText(this, "Please $text", Toast.LENGTH_SHORT).show()
                return
            }
            spinnerValues[i] = text.toInt()
        }
        nutrients = Nutrients(spinnerValues[0], spinnerValues[1], spinnerValues[2], spinnerValues[3])
        Nutrients.setInstance(nutrients)
        val intent = Intent(this, CalculatedNutrients::class.java)
        startActivity(intent)
    }
}