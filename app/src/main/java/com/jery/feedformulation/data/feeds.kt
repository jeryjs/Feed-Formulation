package com.jery.feedformulation.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.jery.feedformulation.utils.Utils
import java.io.File
import java.io.FileOutputStream

data class Feed(
    var name: String,
    var cost: Double,
    var type: String,
    var details: List<Double>,
    var percentage: Map<Int, Double>,
    var checked: Boolean,
    val id: Int
) {
    companion object {
        fun addNewFeed(feed: Feed, feedsFile: File) {
            val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
            val existingArray = JsonParser.parseString(feedsJson).asJsonArray
            val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
            existingArray.add(feedJson)
            val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
            FileOutputStream(feedsFile).use { outputStream ->
                outputStream.write(updatedFeedsJson.toByteArray())
            }
        }

        fun editFeed(feed: Feed, feedsFile: File) {
            val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
            val existingArray = JsonParser.parseString(feedsJson).asJsonArray
            val feedJson = GsonBuilder().setPrettyPrinting().create().toJsonTree(feed).asJsonObject
            existingArray[feed.id] = feedJson
            val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(existingArray)
            FileOutputStream(feedsFile).use { outputStream ->
                outputStream.write(updatedFeedsJson.toByteArray())
            }
        }
    }

    fun getCategoryText(): String {
        return try {
            when (type) {
                "R" -> "Roughages"
                "C" -> "Concentrates"
                "M" -> "Minerals"
                else -> "Other"
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            ""
        }
    }
}
