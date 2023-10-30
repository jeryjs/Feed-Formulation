package com.jery.feedformulation.ui.activities

import android.graphics.drawable.Animatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import com.jery.feedformulation.R
import com.jery.feedformulation.data.Nutrients
import com.jery.feedformulation.databinding.ActivityFormulationBinding
import com.jery.feedformulation.ui.fragments.FeedsFragment
import com.jery.feedformulation.ui.fragments.NutrientFragment
import com.jery.feedformulation.utils.Simplexx

class Formulation : AppCompatActivity() {
    private lateinit var _b: ActivityFormulationBinding
    private val feedsFragment = FeedsFragment()
    private val nutrientFragment = NutrientFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _b = ActivityFormulationBinding.inflate(layoutInflater)
        setContentView(_b.root)

        _b.fabNext.setOnClickListener {
            when (supportFragmentManager.findFragmentById(R.id.fragment_container)) {
                is NutrientFragment -> {
                    val nut = Nutrients.getInstance()
                    var string = _b.tvLog.text.toString()
                    string = "\n\nDATA: ${nut}\n\n"
                    string += "DM:\t${nut.dm}\n"
                    string += "CP:\t${nut.cp}\n"
                    string += "TDN:\t${nut.tdn}\n"
                    _b.tvLog.visibility = View.GONE
                    _b.tvLog.text = string
                    showFeedFragment()
                    supportActionBar?.title = "Select Feeds to be used"
                }
                is FeedsFragment -> {
                    (_b.fabNext.drawable as? Animatable)?.start()
                    if (_b.tvLog.isVisible) { onBackPressed(); return@setOnClickListener }
                    showResultFragment()
                    supportActionBar?.title = "Result"
                }
                else -> {
                    showNutrientFragment()
                    supportActionBar?.title = "Enter Cattle Details"
                }
            }
        }

        _b.fabNext.performClick()
    }

    private fun showNutrientFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, nutrientFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .commit()
    }

    private fun showFeedFragment() {
        val args = Bundle()
        args.putBoolean("IS_SELECT_FEEDS_ENABLED", true)
        feedsFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, feedsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .addToBackStack(null)
            .commit()
    }

    private fun showResultFragment() {
        // Retrieve selected feeds from the FeedsFragment
        val selectedFeeds = feedsFragment.getSelectedFeeds()
        val selectedFeedsIndex = feedsFragment.getSelectedFeedsIndex()

        val s = Simplexx()
        s.solve(selectedFeedsIndex)

        _b.fragmentContainer.visibility = View.GONE
        _b.tvLog.visibility = View.VISIBLE

        var string = "SELECTED FEEDS [${selectedFeeds.size}]:\n\n"
        for ((i, feed) in selectedFeeds.withIndex()) {
            Log.d("FeedSelectionActivity", "Selected Feed: ${feed.name}\t[${feed.details}]")
            string += "x$i:\t\t${feed.name}\t\t[${feed.details}]\n"
        }
        _b.tvLog.text = string

        string += "\nDATA: [${s.ans.size}]\n\n"
        string += "Total DM:\t\t${s.total_dm}\n"
        string += "Total CP:\t\t${s.total_cp}\n"
        string += "Total TDN:\t\t${s.total_tdn}\n\n"
        for ((i, feed) in selectedFeeds.withIndex()) {
            val weight = (s.ans[i]!! * 100.0) / 100.0
            string += "${feed.name}:\t$weight Kg\n"
        }
        _b.tvLog.text = string
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (_b.fragmentContainer.isVisible) super.onBackPressed()
        else {
            _b.fragmentContainer.visibility = View.VISIBLE
            _b.tvLog.visibility = View.GONE
            _b.fabNext.isClickable = true
        }
    }
}