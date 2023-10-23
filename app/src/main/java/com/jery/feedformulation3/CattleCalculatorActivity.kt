package com.jery.feedformulation3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jery.feedformulation3.ui.cattle.*

class CattleCalculatorActivity : AppCompatActivity() {
    companion object {
        val listOfCattle = arrayOf("Crossbred Diary Cow", "Milch Buffalo")
    }

    private lateinit var cattleSelectionFragment: CattleSelectionFragment
    private lateinit var feedFormulationFragment: FeedFormulationFragment
    private lateinit var nutrientReqFragment: NutrientReqFragment
    private lateinit var feedListFragment: FeedListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cattle_calculator)

        // Initialize the fragments
        cattleSelectionFragment = CattleSelectionFragment()
        feedFormulationFragment = FeedFormulationFragment()
        nutrientReqFragment = NutrientReqFragment()
        feedListFragment = FeedListFragment()

        // Show the initial fragment
        showCattleSelectionFragment()
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }
    private fun showCattleSelectionFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.cattleContainer, cattleSelectionFragment)
            .commit()
    }
}
