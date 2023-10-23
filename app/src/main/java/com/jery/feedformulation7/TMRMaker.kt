package com.jery.feedformulation7

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class TMRMaker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.TMR_title)
        setContentView(R.layout.activity_tmrmaker)
    }

    fun GetDetail(v: View) {
        if (R.id.cow === v.id) {
            Log.v("Clicked", "Cow")
            Nutrients.type = 0
        } else if (R.id.buffalo === v.id) {
            Log.v("Clicked", "Buffalo")
            Nutrients.type = 1
        }
        val intent = Intent(this, TMR_Data::class.java)
        startActivity(intent)
    }
}