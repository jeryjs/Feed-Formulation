package com.jery.feedformulation.model

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.io.File
import java.io.FileOutputStream

data class Feed(
    var name: String,
    var cost: Double,
    var type: String,
    var details: List<Double>,
    var percentage: List<Double>,
    var checked: Boolean,
    val id: Int
) {
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
