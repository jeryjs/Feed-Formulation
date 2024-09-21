package com.jery.feedformulation.ui.adapter

import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.jery.feedformulation.databinding.ItemCategoryBinding
import com.jery.feedformulation.databinding.ItemFeedBinding
import com.jery.feedformulation.model.Feed

private const val VIEW_TYPE_CATEGORY = 0
private const val VIEW_TYPE_FEED = 1

class FeedAdapter(
    feedsLiveData: LiveData<List<Feed>>,
    lifecycleOwner: LifecycleOwner,
    private val isSelectFeedsEnabled: Boolean,
    private val onFeedSelected: (Feed) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var feeds: List<Feed> = emptyList()
    private lateinit var catIndex: IntArray // category index

    init {
        feedsLiveData.observe(lifecycleOwner) { updatedFeeds ->
            feeds = updatedFeeds
            notifyDataSetChanged()
        }
    }

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
            VIEW_TYPE_CATEGORY -> CategoryViewHolder(
                ItemCategoryBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_FEED -> FeedViewHolder(ItemFeedBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedViewHolder -> {
                holder.bind(getFeedItem(position))
                holder.itemView.setOnLongClickListener {
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

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.categoryTextView.text = category
        }
    }

    inner class FeedViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isExpanded: Boolean = false

        fun bind(feed: Feed) {
            setFeedSelection(feed)
            updateFeedDetails(feed)
        }

        private fun setFeedSelection(feed: Feed) {
            if (isSelectFeedsEnabled) {
                binding.checkBox.visibility = View.VISIBLE
                binding.checkBox.isChecked = feed.checked
                binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    feed.checked = isChecked
                    onFeedSelected(feed)
                }
                itemView.setOnClickListener { binding.checkBox.performClick() }
            } else {
                binding.imageView.visibility = View.VISIBLE
                itemView.setOnClickListener { expandFeed() }
            }
        }

        private fun updateFeedDetails(feed: Feed) {
            binding.feed = feed
        }

        private fun expandFeed() {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup, ChangeBounds())
            (binding.imageView.drawable as? Animatable)?.start()
            if (isExpanded) {
                binding.detailsLayout.visibility = View.VISIBLE
                binding.imageView.rotation = 0F
            } else {
                binding.detailsLayout.visibility = View.GONE
                binding.imageView.rotation = 270F
            }
        }
    }
}