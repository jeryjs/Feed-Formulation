package com.jery.feedformulation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.net.Uri
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.gson.GsonBuilder
import com.jery.feedformulation.R
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.databinding.DialogAddNewFeedBinding
import com.jery.feedformulation.databinding.ItemCategoryBinding
import com.jery.feedformulation.databinding.ItemFeedBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException

private const val VIEW_TYPE_CATEGORY = 0
private const val VIEW_TYPE_FEED = 1

class FeedAdapter(
    private val feeds: MutableList<Feed>,
    private val feedsFile: File,
    private val IS_SELECT_FEEDS_ENABLED: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var catIndex: IntArray // category index

    override fun getItemCount(): Int {
        catIndex = feeds.map { it.type }.distinct().mapIndexed { index, type ->
            feeds.indexOfFirst { it.type == type } + index
        }.toIntArray()
        return feeds.size + catIndex.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (catIndex.contains(position)) VIEW_TYPE_CATEGORY else VIEW_TYPE_FEED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> CategoryViewHolder(ItemCategoryBinding.inflate(inflater, parent, false))
            VIEW_TYPE_FEED -> FeedViewHolder(ItemFeedBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedViewHolder -> {
                holder.bind(getFeedItem(position))
                holder.itemView.setOnLongClickListener {
                    holder.showDeleteFeedDialog(getFeedItem(position))
                    true
                }
            }
            is CategoryViewHolder -> holder.bind(getFeedItem(position + 1).getCategoryText())
        }
    }

    private fun getFeedItem(position: Int): Feed {
        val index = catIndex.indexOfFirst { it >= position }
        return if (index != -1) feeds[position - index] else feeds[position - catIndex.size]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFeeds(updatedFeeds: List<Feed>) {
        feeds.clear()
        feeds.addAll(updatedFeeds)
        notifyDataSetChanged()
    }

    fun getFeeds(): List<Feed> {
        return feeds
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.categoryTextView.text = category
        }
    }

    inner class FeedViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isExpanded: Boolean = false

        fun bind(feed: Feed) {
            setFeedSelection(feed)
            updateFeedDetails(feed)
            enableCostEditing(feed)
        }

        private fun setFeedSelection(feed: Feed) {
            if (IS_SELECT_FEEDS_ENABLED) {
                binding.checkBox.visibility = View.VISIBLE
                binding.checkBox.isChecked = feed.checked
                itemView.setOnClickListener {selectFeed(feed)}
            } else {
                binding.imageView.visibility = View.VISIBLE
                itemView.setOnClickListener {expandFeed()}
            }
        }

        private fun updateFeedDetails(feed: Feed) {
            try {
                binding.feedNameTextView.text = feed.name
                binding.feedCostTextView.text = "₹ ${feed.cost}"
                val detailsText = "DM: ${feed.details[0]}% \t CP: ${feed.details[1]}% \t TDN: ${feed.details[2]}% \n CA: ${feed.details[3]}% \t PH: ${feed.details[4]}% \t PER: ${feed.percentage}%"
                binding.detailsTextView.text = detailsText
            } catch (e: NullPointerException) {
                corruptedFeedsList(itemView.context)
            }
        }

        private fun enableCostEditing(feed: Feed) {
            binding.feedCostTextView.setOnClickListener {
                showCostEditDialog(feed)
            }
        }

        private fun showCostEditDialog(feed: Feed) {
            val costEditText = EditText(itemView.context)
            costEditText.hint = feed.cost.toString()
            costEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

            val dialog = AlertDialog.Builder(itemView.context)
                .setTitle("Set new cost")
                .setView(costEditText)
                .setPositiveButton("Save") { _, _ ->
                    val newCost = costEditText.text.toString().toDouble()
                    updateCost(newCost)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
            costEditText.requestFocus()
            costEditText.setSelection(costEditText.text.length)
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        private fun updateCost(newCost: Double) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val feed = feeds[position]
                feed.cost = newCost
                saveFeedToJson(feed)
                notifyItemChanged(position)
            }
        }

        private fun expandFeed() {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup, ChangeBounds())
            (binding.imageView.drawable as? Animatable)?.start()
            if (isExpanded) {
                binding.detailsLayout.visibility = View.VISIBLE
                binding.imageView.rotation = 180F
            } else {
                binding.detailsLayout.visibility = View.GONE
                binding.imageView.rotation = 0F
            }
        }

        private fun selectFeed(feed: Feed) {
            binding.checkBox.isChecked = (!binding.checkBox.isChecked)
            feed.checked = binding.checkBox.isChecked
        }

        fun showDeleteFeedDialog(feed: Feed) {
            AlertDialog.Builder(itemView.context)
                .setTitle("Modify Feed")
                .setMessage("How do you want to modify this feed?")
                .setPositiveButton("Delete") { _, _ -> deleteFeed(feed) }
                .setNegativeButton("Edit") { _, _ -> editFeed(feed) }
                .setNeutralButton("Cancel", null)
                .show()
        }

        private fun deleteFeed(feed: Feed) {
            val position = feeds.indexOf(feed)
            if (position != -1) {
                feeds.removeAt(position)
                notifyItemRemoved(position)
                val updatedFeeds = feeds.map { if (it.id == feed.id) feed else it }
                val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(updatedFeeds)
                try { FileOutputStream(feedsFile).use { outputStream -> outputStream.write(updatedFeedsJson.toByteArray()) }
                } catch (e: IOException) { e.printStackTrace() }
                Toast.makeText(itemView.context, "Feed deleted", Toast.LENGTH_SHORT).show()
            }
        }

        private fun editFeed(feed: Feed) {
            val ctx = itemView.context
            val _v = DialogAddNewFeedBinding.inflate(LayoutInflater.from(ctx))

            val detailsFields = listOf(_v.edtDM, _v.edtCP, _v.edtTDN, _v.edtCa, _v.edtPh)
            val percentageFields = listOf(_v.edtMinIncl1, _v.edtMinIncl2)
            val requiredFields = listOf(_v.edtName, _v.edtCost, _v.sprType) + detailsFields.take(3)

            _v.sprType.setAdapter(ArrayAdapter.createFromResource(ctx, R.array.type_array, android.R.layout.simple_spinner_dropdown_item))

            // Populate fields with feed details
            _v.apply {
                edtName.setText(feed.name)
                edtCost.setText(feed.cost.toString())
                cbChecked.isChecked = feed.checked
                detailsFields.forEachIndexed { i, it ->  it.setText(feed.details[i].toString()) }
                percentageFields.forEachIndexed { i, it -> it.setText(feed.percentage[i].toString()) }
                sprType.setText(feed.getCategoryText(), false)
            }

            val dialog = AlertDialog.Builder(ctx)
                .setView(_v.root)
                .setTitle("Edit Feed #${feed.id}")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Help") { _, _ -> startActivity(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=how+to+calculate+feed+nutrients")), null ) }
                .setCancelable(false)
                .show()

            val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.isEnabled = true // Enable by default

            requiredFields+percentageFields.forEach { field ->
                field.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        saveButton.isEnabled = requiredFields.all { it.text.isNotEmpty() } && percentageFields.any { it.text!!.isNotEmpty() }
                    }
                })
            }

            saveButton.setOnClickListener {
                // Update feed data
                val position = feeds.indexOf(feed)
                feed.apply {
                    name = _v.edtName.text.toString().trim()
                    cost = _v.edtCost.text.toString().toDouble()
                    type = _v.sprType.text.toString().substring(0, 1)
                    details = detailsFields.map { if (it.text!!.isNotEmpty()) it.text.toString().toDouble() else 0.0 }
                    percentage = percentageFields.map { if (it.text!!.isNotEmpty()) it.text.toString().toDouble() else 0.0 }
                    checked = _v.cbChecked.isChecked
                }
                saveFeedToJson(feed)
                notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        private fun saveFeedToJson(feed: Feed) {
            val updatedFeeds = feeds.map { if (it.id == feed.id) feed else it }
            val updatedFeedsJson = GsonBuilder().setPrettyPrinting().create().toJson(updatedFeeds)
            try {
                FileOutputStream(feedsFile).use { outputStream ->
                    outputStream.write(updatedFeedsJson.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        private fun corruptedFeedsList(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Feeds List Corrupted")
                .setMessage("The feeds list seems to be corrupted. You need to reset to proceed. This cannot be undone.")
                .setPositiveButton("Reset") { _, _ ->
                    feedsFile.delete()
                    Toast.makeText(context, "Feeds list reset successfully", Toast.LENGTH_SHORT).show()
//                    _FF.finish()
                }
                .setNegativeButton("Cancel") { _, _ -> /*_FF.finish()*/}
                .setCancelable(false)
                .show()
        }
    }
}
