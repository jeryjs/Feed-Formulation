package com.jery.feedformulation.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation.model.Feed
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun loadJSONFromFile(filePath: String): String {
        return try {
            FileInputStream(File(filePath)).use { inputStream ->
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                String(buffer, Charset.forName("UTF-8"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun writeJsonToFile(file: File, json: String) {
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(json.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun generateTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}