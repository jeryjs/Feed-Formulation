package com.jery.feedformulation.ui.activities

import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jery.feedformulation.R
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.databinding.ActivityFormulationBinding
import com.jery.feedformulation.ui.fragments.FeedsFragment
import com.jery.feedformulation.ui.fragments.NutrientFragment
import com.jery.feedformulation.ui.fragments.ResultFragment
import com.jery.feedformulation.utils.Constants as c

class Formulation : AppCompatActivity() {
    private lateinit var _b: ActivityFormulationBinding
    private lateinit var cattle: String
    private val feedsFragment = FeedsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _b = ActivityFormulationBinding.inflate(layoutInflater)
        cattle = intent.action ?: c.CATTLE_COW
        setContentView(_b.root)

        _b.fabNext.setOnClickListener {
            when (supportFragmentManager.findFragmentById(R.id.fragment_container)) {
                is NutrientFragment -> {
                    showFeedFragment()
                    supportActionBar?.title = "Select Feeds to be used"
                }
                is FeedsFragment -> {
                    (_b.fabNext.drawable as? Animatable)?.start()
                    _b.fabNext.rotation += 180f
                    showResultFragment(feedsFragment.getSelectedFeeds(), feedsFragment.getSelectedFeedsIndex())
                    supportActionBar?.title = "Result"
                }
                is ResultFragment ->{
                    (_b.fabNext.drawable as? Animatable)?.start()
                    _b.fabNext.rotation += 180f
                    onBackPressed()
                    supportActionBar?.title = "Select Feeds to be used"
                }
                else -> {
                    showNutrientFragment()
                    supportActionBar?.title = "Select Cattle Details [$cattle]"
                }
            }
        }
        _b.fabNext.performClick()   // to show the first fragment
    }

    private fun showNutrientFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.fragment_container, NutrientFragment(cattle))
            .commit()
    }

    private fun showFeedFragment() {
        val args = Bundle()
        args.putBoolean(c.IS_SELECT_FEEDS_ENABLED, true)
        feedsFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.fragment_container, feedsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showResultFragment(feedsList: List<Feed>, feedsId: List<Int>) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.fragment_container, ResultFragment(feedsList, feedsId))
            .addToBackStack(null)
            .commit()
    }
}