package com.jery.feedformulation.ui.feedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jery.feedformulation.databinding.FragmentFeedsBinding
import com.jery.feedformulation.ui.adapter.FeedAdapter
import com.jery.feedformulation.viewmodel.FeedsViewModel

class FeedsFragment : Fragment() {

    private var _binding: FragmentFeedsBinding? = null
    private val binding get() = _binding!!
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedsViewModel = ViewModelProvider(this)[FeedsViewModel::class.java]

        setupRecyclerView()
        observeFeeds()
    }

    private fun setupRecyclerView() {
        feedAdapter = FeedAdapter(
            mutableListOf(), false,
            onFeedSelected = {  },
            onFeedLongClicked = {  }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = feedAdapter
    }

    private fun observeFeeds() {
        feedsViewModel.feeds.observe(viewLifecycleOwner) { feeds ->
            feedAdapter.updateFeeds(feeds)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}