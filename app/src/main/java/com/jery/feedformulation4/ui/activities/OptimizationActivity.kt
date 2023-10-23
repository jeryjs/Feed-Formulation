package com.jery.feedformulation4.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation4.R
import com.jery.feedformulation4.data.Feed
import com.jery.feedformulation4.utils.Solver
import com.jery.feedformulation4.utils.Utils
import java.io.File

class OptimizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimization)

        val dm = 13.76
        val cp = 1395.59
        val tdn = 6605.82

        val feedsJson = Utils.loadJSONFromFile(File(filesDir, "feeds.json").path)
        val feedType = object : TypeToken<List<Feed>>() {}.type
        val feeds: List<Feed> = Gson().fromJson(feedsJson, feedType)
        val costs = feeds.map { it.cost }
        val details: List<List<Double>> = feeds.map { it.details }

        val solver = Solver(details, costs, dm, cp, tdn)
    }
}