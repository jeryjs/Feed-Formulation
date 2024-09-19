package com.jery.feedformulation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.jery.feedformulation.databinding.ItemCategoryBinding
import com.jery.feedformulation.databinding.ItemFeedBinding
import com.jery.feedformulation.model.Feed

private const val VIEW_TYPE_CATEGORY = 0
private const val VIEW_TYPE_FEED = 1

class FeedAdapter(
    private val feeds: MutableList<Feed>,
    private val isSelectFeedsEnabled: Boolean,
    private val onFeedSelected: (Feed) -> Unit,
    private val onFeedLongClicked: (Feed) -> Unit
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
                    onFeedLongClicked(getFeedItem(position))
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
        }

        private fun setFeedSelection(feed: Feed) {
            if (isSelectFeedsEnabled) {
                binding.checkBox.visibility = View.VISIBLE
                binding.checkBox.isChecked = feed.checked
                itemView.setOnClickListener { onFeedSelected(feed) }
            } else {
                binding.imageView.visibility = View.VISIBLE
                itemView.setOnClickListener { expandFeed() }
            }
        }

        private fun updateFeedDetails(feed: Feed) {
            binding.feedNameTextView.text = feed.name
            binding.feedCostTextView.text = "₹ ${feed.cost}"
            val detailsText = "DM: ${feed.details[0]}% \t CP: ${feed.details[1]}% \t TDN: ${feed.details[2]}% \n CA: ${feed.details[3]}% \t PH: ${feed.details[4]}% \t PER: ${feed.percentage}%"
            binding.detailsTextView.text = detailsText
        }

        private fun expandFeed() {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup, ChangeBounds())
            if (isExpanded) {
                binding.detailsLayout.visibility = View.VISIBLE
                binding.imageView.rotation = 180F
            } else {
                binding.detailsLayout.visibility = View.GONE
                binding.imageView.rotation = 0F
            }
        }
    }
}