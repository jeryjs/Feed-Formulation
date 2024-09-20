package com.jery.feedformulation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.model.Nutrients

class TmrmakerViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedCattle = MutableLiveData<String>()
    val selectedCattle: LiveData<String> get() = _selectedCattle

    private val _selectedNutrients = MutableLiveData<Nutrients>()
    val selectedNutrients: LiveData<Nutrients> get() = _selectedNutrients

    private val _selectedFeeds = MutableLiveData<List<Feed>>()
    val selectedFeeds: LiveData<List<Feed>> get() = _selectedFeeds

    private val _result = MutableLiveData<List<Double>>()
    val result: LiveData<List<Double>> get() = _result

    fun selectCattle(cattle: String) {
        _selectedCattle.value = cattle
    }

    fun selectNutrients(nutrients: Nutrients) {
        _selectedNutrients.value = nutrients
    }

    fun selectFeeds(feeds: List<Feed>) {
        _selectedFeeds.value = feeds
    }

    fun calculateResult() {
        _selectedNutrients.value?.let {
            _result.value = it.calculateNutrients()
        }
    }
}