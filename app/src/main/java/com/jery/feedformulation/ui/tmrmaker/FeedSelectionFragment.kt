package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jery.feedformulation.databinding.FragmentFeedSelectionBinding
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.ui.adapter.FeedAdapter
import com.jery.feedformulation.viewmodel.FeedsViewModel
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class FeedSelectionFragment : Fragment() {

    private var _binding: FragmentFeedSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel
    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        feedsViewModel = ViewModelProvider(requireActivity())[FeedsViewModel::class.java]
        _binding = FragmentFeedSelectionBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val feedAdapter = FeedAdapter(
            feedsViewModel.feeds,
            viewLifecycleOwner,
            isSelectFeedsEnabled = true,
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = feedAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}