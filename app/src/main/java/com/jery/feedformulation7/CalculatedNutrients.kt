package com.jery.feedformulation7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class CalculatedNutrients : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculated_nutrients)

        if (Nutrients.type == 0) setTitle(R.string.cow_title) else setTitle(R.string.buffalo_title)
        setContentView(R.layout.activity_calculated_nutrients)

        val nutrients = Nutrients.getInstance()
        nutrients!!.calculateNutrients()

        val dm = (nutrients.DM * 100.0).roundToInt() / 100.0
        val cp = (nutrients.CP * 100.0).roundToInt() / 100.0
        val tdn = (nutrients.TDN * 100.0).roundToInt() / 100.0

        (findViewById<View>(R.id.DM) as TextView).text = "$dm to  ${dm + 1.0}"
        (findViewById<View>(R.id.CP) as TextView).text = "$cp to  ${(cp * 1.1 * 100.0) / 100.0}"
        (findViewById<View>(R.id.TDN) as TextView).text = "$tdn to  ${(tdn * 1.1 * 100.0) / 100.0}"

        (findViewById<Button>(R.id.selectfeedlist)).setOnClickListener {
            startActivity(Intent(this, FeedList::class.java))
        }
    }
}