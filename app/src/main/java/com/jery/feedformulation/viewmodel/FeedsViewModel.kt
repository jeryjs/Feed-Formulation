package com.jery.feedformulation.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.jery.feedformulation.R
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.utils.FeedUtils
import com.jery.feedformulation.utils.Utils
import kotlinx.coroutines.launch
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
        val feeds: List<Feed> = FeedUtils.parseJsonToList(feedsJson)
        val sortedFeeds = FeedUtils.sortFeedsByType(feeds)
        _feeds.value = sortedFeeds
    }

    fun addNewFeed(feed: Feed) {
        viewModelScope.launch {
            val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
            val updatedFeedsJson = FeedUtils.addFeedToJson(feedsJson, feed)
            Utils.writeJsonToFile(feedsFile, updatedFeedsJson)
            _feeds.value = _feeds.value?.plus(feed)
        }
    }

    fun exportFeeds(uri: Uri) {
        viewModelScope.launch {
            try {
                getApplication<Application>().contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
                    outputStream.write(feedsJson.toByteArray())
                    Toast.makeText(getApplication(), "Feeds list exported successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(getApplication(), "Failed to export feeds list: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun importFeeds(uri: Uri) {
        viewModelScope.launch {
            try {
                getApplication<Application>().contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(feedsFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                        Toast.makeText(getApplication(), "Feeds list imported successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                loadFeedsList()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(getApplication(), "Failed to import feeds list: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resetFeeds() {
        viewModelScope.launch {
            feedsFile.delete()
            Toast.makeText(getApplication(), "Feeds list reset successfully", Toast.LENGTH_SHORT).show()
            initializeFeedsFile()
            loadFeedsList()
        }
    }
}