package com.jery.feedformulation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.jery.feedformulation.databinding.ActivityMainBinding
import com.jery.feedformulation.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        SettingsFragment().applySettings(sharedPreferences) // Restore settings at launch

        val isBottomNav = sharedPreferences.getBoolean("use_bottom_nav", true)

        val navController = findNavController(R.id.nav_host_fragment)
        setupNavigation(navController, isBottomNav)

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == "use_bottom_nav") {
                val isBottomNavUpdated = sharedPreferences.getBoolean(key, false)
                setupNavigation(navController, isBottomNavUpdated)
                this.recreate()
            }
        }
    }

    private fun setupNavigation(navController: NavController, isBottomNav: Boolean) {
        if (isBottomNav) {
            binding.bottomNav.visibility = View.VISIBLE
            setupBottomNav(navController)
        } else {
            binding.bottomNav.visibility = View.GONE
            setupSideNav(navController)
        }
    }

    private fun setupBottomNav(navController: NavController) {
        val bottomNavView: BottomNavigationView = binding.bottomNav
        bottomNavView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_feeds, R.id.nav_tmrmaker, R.id.nav_more)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        drawerToggle.isDrawerIndicatorEnabled = false
    }

    private fun setupSideNav(navController: NavController) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_feeds, R.id.nav_tmrmaker, R.id.nav_settings), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        drawerToggle.isDrawerIndicatorEnabled = true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}