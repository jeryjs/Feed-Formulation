package com.jery.feedformulation.ui.activities

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.ActivityFeedsSelectionBinding
import com.jery.feedformulation.ui.fragments.FeedsFragment
import com.jery.feedformulation.utils.Simplex

class FeedsSelection : AppCompatActivity() {
    private lateinit var binding: ActivityFeedsSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedsSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create instance of FeedsFragment
        val feedsFragment = FeedsFragment.newInstance(true)

        // Add the fragment to the fragment container
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, feedsFragment)
            .commit()

        // Set click listener for FAB
        binding.fabSelectFeeds.setOnClickListener {
            // Retrieve selected feeds from the FeedsFragment
            binding.fabSelectFeeds.isClickable = false
            (binding.fabSelectFeeds.drawable as? Animatable)?.start()
            val selectedFeeds = feedsFragment.getSelectedFeeds()

            val s = Simplex()
            s.Solver(selectedFeeds)

            binding.fragmentContainer.visibility = View.GONE
            binding.tvLog.visibility = View.VISIBLE

            var string = ""
            for ((index, feed) in selectedFeeds.withIndex()) {
                Log.d("FeedSelectionActivity", "Selected Feed: ${feed.name}\t[${feed.details}]")
                string += "x$index:\t\t${feed.name}\t\t[${feed.details}]\n"
            }
            binding.tvLog.text = string

            string += "\n\nDATA:\n\n"
            string += "Total DM:\t\t${s.total_dm}\n"
            string += "Total CP:\t\t${s.total_cp}\n"
            string += "Total TDN:\t\t${s.total_tdn}\n\n"
            for (i in selectedFeeds.indices) {
                val weight = (s.ans[i]!! * 100.0) / 100.0
                string += "${selectedFeeds[i].name}:\t$weight Kg\n"
            }
            binding.tvLog.text = string
        }
//        binding.fabSelectFeeds.performClick()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.fragmentContainer.isVisible) super.onBackPressed()
        else {
            binding.fragmentContainer.visibility = View.VISIBLE
            binding.tvLog.visibility = View.GONE
            binding.fabSelectFeeds.isClickable = true
            (binding.fabSelectFeeds.drawable as? Animatable)?.start()
        }
    }
}