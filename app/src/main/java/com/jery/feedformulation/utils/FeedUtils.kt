package com.jery.feedformulation.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation.model.Feed

object FeedUtils {
    fun parseJsonToList(json: String): List<Feed> {
        val feedType = object : TypeToken<List<Feed>>() {}.type
        return Gson().fromJson(json, feedType)
    }

    fun addFeedToJson(json: String, feed: Feed): String {
        val existingArray = JsonParser.parseString(json).asJsonArray
        val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
        existingArray.add(feedJson)
        return GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
    }

    fun editFeedInJson(json: String, feed: Feed): String {
        val existingArray = JsonParser.parseString(json).asJsonArray
        val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
        existingArray[feed.id] = feedJson
        return GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
    }

    fun sortFeedsByType(feeds: List<Feed>): List<Feed> {
        val typeSortingWeights = mapOf("R" to 0, "C" to 1, "M" to 2, "" to 3)
        return feeds.sortedBy { typeSortingWeights[it.type] }
    }
}