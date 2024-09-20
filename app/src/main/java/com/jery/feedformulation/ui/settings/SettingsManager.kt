package com.jery.feedformulation.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import java.util.*

object SettingsManager {

    fun applyTheme(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val isNightMode = sharedPreferences.getBoolean("theme_switch", false)
        print(isNightMode)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun applyLanguage(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = sharedPreferences.getString("language", "en") ?: "en"
        setLocale(context, language)
    }

    private fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun applyAllSettings(context: Context) {
        applyTheme(context)
        applyLanguage(context)
    }
}