package com.jery.feedformulation5

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation5.data.Feed
import com.jery.feedformulation5.databinding.ActivityMainBinding
import com.jery.feedformulation5.databinding.FeedItemLayoutBinding
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val B = ActivityMainBinding.inflate(layoutInflater)
        setContentView(B.root)

        // Read the JSON file and parse the data
        val feedsJson = loadJSONFromAsset("feeds.json")
        val feedType = object : TypeToken<List<Feed>>() {}.type
        val feeds: List<Feed> = Gson().fromJson(feedsJson, feedType)

        // Sort the feeds list in the order R, C, M, O to workaround the category grouping of the feeds
        val typeSortingWeights = mapOf("R" to 0, "C" to 1, "M" to 2, "" to 3)
        val sortedFeeds = feeds.sortedBy { typeSortingWeights[it.type] }

        // Set up the RecyclerView
        B.recyclerView.layoutManager = LinearLayoutManager(this)
        B.recyclerView.adapter = FeedAdapter(sortedFeeds)
    }

    private fun loadJSONFromAsset(fileName: String): String {
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    inner class FeedAdapter(private val feeds: List<Feed>) :
        RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item_layout, parent, false)
            return FeedViewHolder(view)
        }

        override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
            val feed = feeds[position]
            holder.bind(feed)
        }

        override fun getItemCount() = feeds.size

        inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val _B = FeedItemLayoutBinding.bind(itemView)
            private var isExpanded: Boolean = false

            init {
                itemView.setOnClickListener {
                    isExpanded = !isExpanded
                    onFeedItemClick()
                }
            }

            fun bind(feed: Feed) {
                // Set the feed category based on its type
                val category = when (feed.type) {
                    "R" -> "Roughages"
                    "C" -> "Concentrates"
                    "M" -> "Minerals"
                    else -> "Other"
                }

                // Update the category TextView visibility and text
                if (position == 0 || feed.type != feeds[position - 1].type) {
                    _B.categoryTextView.visibility = View.VISIBLE
                    _B.categoryTextView.text = category
                } else {
                    _B.categoryTextView.visibility = View.GONE
                }

                // Set the feed name and cost
                _B.feedNameTextView.text = feed.name
                _B.feedCostTextView.text = "₹ ${feed.cost}"

                // Set the details text
                val detailsText =
                    "DM: ${feed.details[0]}% \t CP: ${feed.details[1]}% \t TDN: ${feed.details[2]}%\nCa: ${feed.details[3]}% \t P: ${feed.details[4]}%"
                _B.detailsTextView.text = detailsText

                // Enable cost editing
                _B.feedCostTextView.setOnClickListener {
                    showCostEditDialog(feed)
                }
            }

            private fun showCostEditDialog(feed: Feed) {
                val costEditText = EditText(itemView.context)
                costEditText.hint = feed.cost
                costEditText.setSingleLine()

                val dialog = AlertDialog.Builder(itemView.context)
                    .setTitle("Set new cost")
                    .setView(costEditText)
                    .setPositiveButton("Save") { _, _ ->
                        val newCost = costEditText.text.toString().trim()
                        updateCost(newCost)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()

                dialog.show()
                costEditText.requestFocus()
                costEditText.setSelection(costEditText.text.length)
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }

            private fun updateCost(newCost: String) {
                // Perform the necessary operations to update the cost
                // For example, you can update the Feed object and notify the adapter of the change
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val feed = feeds[position]
                    feed.cost = newCost
                    notifyItemChanged(position)
                }
            }

            private fun onFeedItemClick() {
                TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup, ChangeBounds())
                if (isExpanded) {
                    _B.detailsLayout.visibility = View.VISIBLE
                    _B.imageView.rotation = 180F
                } else {
                    _B.detailsLayout.visibility = View.GONE
                    _B.imageView.rotation = 0F
                }
            }
        }
    }

    fun addNewFeed(view: View) {
        startActivity(Intent(this, NewFeedActivity::class.java))
    }
}
