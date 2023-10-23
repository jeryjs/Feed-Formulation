package com.jery.feedformulation4.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jery.feedformulation4.R
import com.jery.feedformulation4.data.Feed
import com.jery.feedformulation4.databinding.ActivityFeedsListBinding
import com.jery.feedformulation4.databinding.DialogNewFeedBinding
import com.jery.feedformulation4.ui.adapters.FeedAdapter
import com.jery.feedformulation4.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.properties.Delegates

private const val EXPORT_FEEDS_REQUEST_CODE = 100
private const val IMPORT_FEEDS_REQUEST_CODE = 101

class FeedsListActivity : AppCompatActivity() {
    private lateinit var feedsFile: File
    private lateinit var binding: ActivityFeedsListBinding
    private lateinit var feedAdapter: FeedAdapter
    var IS_SELECT_FEEDS_ENABLED: Boolean by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedsListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        feedsFile = File(filesDir, "feeds.json")
        IS_SELECT_FEEDS_ENABLED = intent.getBooleanExtra("IS_SELECT_FEEDS_ENABLED", false)

        setupRecyclerView()
        initializeFeedsFile()
        loadFeedsList()
    }

    private fun setupRecyclerView() {
        feedAdapter = FeedAdapter(mutableListOf(), feedsFile, IS_SELECT_FEEDS_ENABLED)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = feedAdapter
    }

    private fun initializeFeedsFile() {
        if (!feedsFile.exists()) {
            assets.open(feedsFile.name).use { input ->
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
        feedAdapter.updateFeeds(sortedFeeds)
    }

    fun addNewFeed(v: View) {
        val _v = DialogNewFeedBinding.inflate(layoutInflater)
        val typeArray = resources.getStringArray(R.array.type_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeArray)
        _v.sprType.setAdapter(adapter)
        _v.sprType.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) Utils.hideKeyboard(view)
            _v.sprType.showDropDown()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(_v.root)
            .setTitle("Add New Feed #${feedAdapter.itemCount+1}")
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Help") { _, _ -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/gview?url=https://www.nddb.coop/sites/default/files/pdfs/Animal-Nutrition-booklet.pdf"))) }
            .setCancelable(false)
            .show()

        val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton.isEnabled = false

        val requiredFields = listOf( _v.edtName, _v.edtCost, _v.sprType, _v.edtDM, _v.edtCP, _v.edtTDN, _v.edtMinInclLvl )
        requiredFields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    saveButton.isEnabled = requiredFields.all { it.text.isNotEmpty() }
                }
            })
        }

        saveButton.setOnClickListener {
            val newFeed = Feed(
                name = _v.edtName.text.toString().trim(),
                cost = _v.edtCost.text.toString().toDouble(),
                type = _v.sprType.text.toString().substring(0, 1),
                details = listOf(_v.edtDM, _v.edtCP, _v.edtTDN).map { it.text.toString().toDouble() },
                percentages = listOf(
                    mapOf("cow" to _v.edtMinInclLvl.text.toString().toDouble()),
                    mapOf("buffalo" to _v.edtMinInclLvl.text.toString().toDouble())
                ),
                checked = false,
                id = feedAdapter.itemCount+1
            )
            Feed.addNewFeed(newFeed, feedsFile)
            loadFeedsList()
            dialog.dismiss()
        }
    }

    fun exportFeedsList(v: View) {
        val timeStamp = Utils.generateTimestamp()
        val fileName = "feeds_export_$timeStamp.json"

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        startActivityForResult(intent, EXPORT_FEEDS_REQUEST_CODE)
    }

    fun importFeedsList(v: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        startActivityForResult(intent, IMPORT_FEEDS_REQUEST_CODE)
    }

    fun resetFeedsList(v: View?) {
        AlertDialog.Builder(this)
            .setTitle("Reset Feeds List")
            .setMessage("Are you sure you want to reset the feeds list? This action cannot be undone.")
            .setPositiveButton("Reset") { _, _ ->
                feedsFile.delete()
                Toast.makeText(this, "Feeds list reset successfully", Toast.LENGTH_SHORT).show()
                loadFeedsList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EXPORT_FEEDS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        try {
                            contentResolver.openOutputStream(it)?.use { outputStream ->
                                val feedsJson = Utils.loadJSONFromFile(feedsFile.path)
                                outputStream.write(feedsJson.toByteArray())
                                Toast.makeText(this, "Feeds list exported successfully", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Failed to export feeds list", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            IMPORT_FEEDS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        try {
                            contentResolver.openInputStream(it)?.use { inputStream ->
                                FileOutputStream(feedsFile).use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                    Toast.makeText(this, "Feeds list imported successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Failed to import feeds list", Toast.LENGTH_SHORT).show()
                        }
                        loadFeedsList()
                    }
                }
            }
        }
    }
}
