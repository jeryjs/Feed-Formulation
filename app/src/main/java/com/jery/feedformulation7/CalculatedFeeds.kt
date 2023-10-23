package com.jery.feedformulation7

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class CalculatedFeeds : AppCompatActivity() {

    companion object {
        var feedData: FeedData? = null
    }

    private val feedNames = arrayOf(
        "Paddy straw", "CO-4 grass", "Maize fodder", "Co Fs 29 sorghum fodder", "Ragi Straw",
        "Berseem", "Wheat straw", "Maize stover", "Maize", "Soya DOC", "Copra DOC", "Cotton DOC",
        "Wheat Bran", "Gram Chunies", "cotton seed", "chickpeahusk", "Concentrate Mix Type I ",
        "Concentrate mix Type II", "Calcite", "Grit", "MM", "DCP", "SodaBicarb", "Salt", "TM Mix", "Urea"
    )

    private lateinit var feedModel: ArrayList<ResultList>
    private lateinit var adapter: ResultListAdapter
    private lateinit var mContext: Context
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculated_feeds)

        feedData = FeedData.getInstance()

        mContext = this

        listView = findViewById(R.id.result_list)

        try {
            val s = Simplex()
            s.Solver()

            Log.v("Total DM", "${s.total_dm}")
            Log.v("Total CP", "${s.total_cp}")
            Log.v("Total TDN", "${s.total_tdn}")

            feedModel = ArrayList()
            for (i in feedData!!.feedsSelected.indices) {
                println(s.ans)
                val weight = (s.ans[i]!! * 100.0).roundToInt() / 100.0
                feedModel.add(
                    ResultList(
                        feedNames[feedData!!.feedsSelected[i]],
                        feedData!!.feedCost[i],
                        weight
                    )
                )
            }
            var totalCost = 0.0
            var totalWeight = 0.0
            for (i in feedModel.indices) {
                totalCost += feedModel[i].totalCost
                totalWeight += feedModel[i].amount
            }

            totalWeight = (totalWeight * 100.0).roundToInt() / 100.0

            feedModel.add(ResultList("Total Weight (g)", totalWeight, -1.0))
            feedModel.add(ResultList("Total Cost (Rs)", totalCost, -1.0))

            adapter = ResultListAdapter(feedModel, applicationContext)
            listView.adapter = adapter
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            findViewById<TextView>(R.id.error).visibility = View.VISIBLE
        }
    }
}