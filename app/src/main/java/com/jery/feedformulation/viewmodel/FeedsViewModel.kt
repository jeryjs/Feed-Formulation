package com.jery.feedformulation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation.R
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FeedsViewModel(application: Application) : AndroidViewModel(application) {

    private val feedsFile: File = File(application.filesDir, "feeds.json")

    private val _feeds = MutableLiveData<List<Feed>>()
    val feeds: LiveData<List<Feed>> get() = _feeds

    init {
        initializeFeedsFile()
        loadFeedsList()
    }

    private fun initializeFeedsFile() {
        if (!feedsFile.exists()) {
            getApplication<Application>().resources.openRawResource(R.raw.feeds).use { input ->
                FileOutputStream(feedsFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun loadFeedsList() {
        val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
        val feedType = object : TypeToken<List<Feed>>() {}.type
        val feeds: List<Feed> = Gson().fromJson(feedsJson, feedType)
        val typeSortingWeights = mapOf("R" to 0, "C" to 1, "M" to 2, "" to 3)
        val sortedFeeds = feeds.sortedBy { typeSortingWeights[it.type] }
        _feeds.value = sortedFeeds
    }

    fun addNewFeed(feed: Feed) {
        val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
        val existingArray = JsonParser.parseString(feedsJson).asJsonArray
        val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
        existingArray.add(feedJson)
        val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
        FileOutputStream(feedsFile).use { outputStream ->
            outputStream.write(updatedFeedsJson.toByteArray())
        }
        _feeds.value = _feeds.value?.plus(feed)
    }

    fun editFeed(feed: Feed) {
        val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
        val existingArray = JsonParser.parseString(feedsJson).asJsonArray
        val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
        existingArray[feed.id] = feedJson
        val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
        FileOutputStream(feedsFile).use { outputStream ->
            outputStream.write(updatedFeedsJson.toByteArray())
        }
        _feeds.value = _feeds.value?.map { if (it.id == feed.id) feed else it }
    }

    fun deleteFeed(feed: Feed) {
        val updatedFeeds = _feeds.value?.filter { it.id != feed.id }
        val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(updatedFeeds)
        try {
            FileOutputStream(feedsFile).use { outputStream ->
                outputStream.write(updatedFeedsJson.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        _feeds.value = updatedFeeds!!
    }
}