package com.jery.feedformulation.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.jery.feedformulation.R
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        // Apply settings at startup
        applySettings(sharedPreferences)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        if (sharedPreferences == null || key == null) return
        applySettings(sharedPreferences!!, key)
    }

    fun applySettings(sharedPreferences: SharedPreferences, key: String? = null) {
        if (key==null || key == "theme_switch") {
            val isNightMode = sharedPreferences.getBoolean("theme_switch", false)
            println("Night mode: $isNightMode")
            AppCompatDelegate.setDefaultNightMode(if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (key==null || key == "language") {
            try {
                val language = sharedPreferences.getString("language", Locale.getDefault().language) ?: Locale.getDefault().language
                if (language != Locale.getDefault().language) {
                    val locale = Locale(language)
                    Locale.setDefault(locale)
                    val config = resources.configuration
                    config.setLocale(locale)
                    resources.updateConfiguration(config, resources.displayMetrics)
                    requireActivity().recreate()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (key==null || key == "use_bottom_nav") {
            val isBottomNav = sharedPreferences.getBoolean("use_bottom_nav", false)
            sharedPreferences.edit().putBoolean("use_bottom_nav", isBottomNav).apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}