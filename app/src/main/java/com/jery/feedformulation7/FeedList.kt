package com.jery.feedformulation7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedList : AppCompatActivity() {

    private lateinit var feedsData: FeedData
    private val feedNames = arrayOf(
        "Paddy straw", "CO-4 grass", "Maize fodder", "Co Fs 29 sorghum fodder", "Ragi Straw",
        "Berseem", "Wheat straw", "Maize stover", "Maize", "Soya DOC", "Copra DOC", "Cotton DOC",
        "Wheat Bran", "Gram Chunies", "cotton seed", "chickpeahusk", "Concentrate Mix Type I ",
        "Concentrate mix Type II", "Calcite", "Grit", "MM", "DCP", "SodaBicarb", "Salt", "TM Mix", "Urea"
    )
    private val feedSelected = booleanArrayOf(true, true, true, false, true, true, false, true, true, true, true, true, true, false, true, true, true, true, true, true, true, false, false, false, false, false)
    private val feedCost = doubleArrayOf(
        3.0, 3.0, 3.0, 3.0, 3.5, 2.0, 2.0, 3.0, 17.0, 38.0, 23.0, 23.0, 17.0, 14.0, 21.0, 10.0,
        17.0, 15.0, 5.0, 4.5, 60.0, 38.0, 35.0, 5.0, 150.0, 6.0
    )

    private lateinit var adapter: ArrayAdapter<FeedListItem>
    private lateinit var feedList: Array<FeedListItem>
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_list)
        mContext = this

        val listView = findViewById<ListView>(R.id.feed_list)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, i, _ ->
                val feedList = adapter.getItem(i) as FeedListItem
                feedList.toggleChecked()
                val viewHolder = view.tag as FeedListHolder

                viewHolder.checkBox.isChecked = feedList.checked
                viewHolder.editText.setText(feedList.cost.toString())
            }

//        println(feedList)
//        feedList = lastNonConfigurationInstance!! as Array<FeedListItem>
//        println(feedList)

        feedList = Array(26) { i -> FeedListItem(feedNames[i], feedSelected[i], feedCost[i]) }
        val feeds = ArrayList<FeedListItem>()
        feeds.addAll(feedList)

        adapter = FeedListAdapter(this, feeds)
        listView.adapter = adapter

        val headerView = LayoutInflater.from(this).inflate(R.layout.feed_list_header, null)
        listView.addHeaderView(headerView)
    }

    fun calculate(v: View) {
        Log.v("Reached", "Start of calculate")

        feedsData = FeedData.getInstance()!!

        feedsData.removeAllFeedsSelected()

        var x = ""

        for (i in 0..25) {
            Log.v("Iteration", "$i")
            val feed = adapter.getItem(i) as FeedListItem
            if (feed.checked) {
                Log.v("Condition", "$i")
                feedsData.addFeedsSelected(i)
                if (-1.0 == feed.cost) {
                    Toast.makeText(this, "Please enter cost for ${feed.name}", Toast.LENGTH_SHORT).show()
                    feedsData.removeAllFeedsSelected()
                    return
                } else {
                    feedsData.addCostEntered(feed.cost)
                }
                x += i
            }
        }

        feedsData.done()

        Log.v("Reached", "calculate")

        Log.v("Feed Names", feedsData.names())
        Log.v("Feed Costs", feedsData.costs())

        Log.d("Testing", FeedData.toString())
        FeedData.setInstance(feedsData)

        val intent = Intent(this, CalculatedFeeds::class.java)
        startActivity(intent)
    }

    private class FeedListItem(var name: String, var checked: Boolean, var cost: Double) {
        fun toggleChecked() {
            checked = !checked
        }
    }

    private class FeedListHolder(var checkBox: CheckBox, var editText: EditText)

    private inner class FeedListAdapter(context: Context, feedList: ArrayList<FeedListItem>) :
        ArrayAdapter<FeedListItem>(context, R.layout.feed_list, feedList) {

        private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val feed = getItem(position)

            var checkBox: CheckBox?
            var editText: EditText?

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.feed_list, null)
                checkBox = convertView.findViewById(R.id.checkBox)
                editText = convertView.findViewById(R.id.editText)

                convertView.tag = FeedListHolder(checkBox, editText)

                checkBox.setOnClickListener {
                    val cb = it as CheckBox
                    val feed = cb.tag as FeedListItem
                    feed.checked = cb.isChecked
                }
            } else {
                val viewHolder = convertView.tag as FeedListHolder

                checkBox = viewHolder.checkBox
                editText = viewHolder.editText
            }

            checkBox?.tag = feed
            editText?.tag = feed

            checkBox?.isChecked = feed?.checked ?: false
            checkBox?.text = feed?.name ?: ""
            if (feed?.cost == -1.0)
                editText?.setText("")
            else
                editText?.setText(feed?.cost.toString())

            editText?.id = position
            editText?.setOnFocusChangeListener { view, b ->
                if (!b) {
                    val position = view.id
                    val et = view as EditText
                    val feed = et.tag as FeedListItem
                    if (!TextUtils.isEmpty(et.text))
                        feed.cost = et.text.toString().toDouble()
                    else
                        feed.cost = -1.0
                }
            }

            return convertView!!
        }
    }
}