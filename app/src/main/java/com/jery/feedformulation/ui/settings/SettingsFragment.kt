package com.jery.feedformulation.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.jery.feedformulation.R
import com.jery.feedformulation.viewmodel.SettingsViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themeSwitch: SwitchPreferenceCompat? = findPreference("theme_switch")
        themeSwitch?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.setNightMode(newValue as Boolean)
            true
        }

        val languagePreference: ListPreference? = findPreference("language")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.setLanguage(newValue as String)
            true
        }
    }
}