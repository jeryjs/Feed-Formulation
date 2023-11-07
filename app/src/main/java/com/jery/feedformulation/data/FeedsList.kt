package com.jery.feedformulation.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation.utils.Utils
import java.io.File

data class FeedsList(
    var feeds: MutableList<Feed> = mutableListOf(),
    val feedsFile: File = File("feeds.json")
) {
    init {
        initializeFeedsFile()
        loadFeedsList()
    }

    companion object {
        private lateinit var instance: FeedsList

        fun getInstance(): FeedsList {
            return if (::instance.isInitialized) instance else FeedsList()
        }
        fun setInstance(f: FeedsList) { instance = f }
    }

    private fun initializeFeedsFile() {
        if (!feedsFile.exists()) {
            // Initialize feedsFile if it doesn't exist
        }
    }

    private fun loadFeedsList() {
        val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
        val feedType = object : TypeToken<List<Feed>>() {}.type
        val feeds: List<Feed> = Gson().fromJson(feedsJson, feedType)
        val typeSortingWeights = mapOf("R" to 0, "C" to 1, "M" to 2, "" to 3)
        val sortedFeeds = feeds.sortedBy { typeSortingWeights[it.type] }
        this.feeds = sortedFeeds.toMutableList()
    }

    fun addNewFeed(newFeed: Feed) {
        feeds.add(newFeed)
        Feed.addNewFeed(newFeed, feedsFile)
    }

    // Add other functions as needed, e.g., exportFeeds, importFeeds, resetFeeds, etc.
}
