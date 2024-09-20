package com.jery.feedformulation.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import java.util.*

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    private val _language = MutableLiveData<String>()
    val language: LiveData<String> get() = _language

    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
        _isNightMode.value = sharedPreferences.getBoolean("theme_switch", false)
        _language.value = sharedPreferences.getString("language", "en")
        setNightMode(_isNightMode.value ?: false)
        setLanguage(_language.value ?: "en")
    }

    fun setNightMode(isNightMode: Boolean) {
        _isNightMode.value = isNightMode
        savePreference("theme_switch", isNightMode)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun setLanguage(language: String) {
        _language.value = language
        println(language)
        savePreference("language", language)
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = getApplication<Application>().resources.configuration
        config.setLocale(locale)
        getApplication<Application>().resources.updateConfiguration(config, getApplication<Application>().resources.displayMetrics)
    }

    private fun savePreference(key: String, value: Any) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
        with(sharedPreferences.edit()) {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported preference type")
            }
            apply()
        }
    }
}