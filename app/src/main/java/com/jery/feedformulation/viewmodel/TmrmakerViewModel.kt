package com.jery.feedformulation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jery.feedformulation.R
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.model.Nutrients
import com.jery.feedformulation.model.SimplexResult

class TmrmakerViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedCattleId = MutableLiveData<Int>()
    val selectedCattleId: LiveData<Int> get() = _selectedCattleId
    val selectedCattle: String get() = getApplication<Application>().resources.getStringArray(R.array.cattle_array)[_selectedCattleId.value ?: 0]

    private val _selectedNutrients = MutableLiveData<Nutrients>()
    val selectedNutrients: LiveData<Nutrients> get() = _selectedNutrients

    private val _selectedFeeds = MutableLiveData<List<Feed>>()
    val selectedFeeds: LiveData<List<Feed>> get() = _selectedFeeds

    private val _result = MutableLiveData<SimplexResult>()
    val result: LiveData<SimplexResult> get() = _result

    fun selectCattle(cattle: Int) {
        _selectedCattleId.value = cattle
    }

    fun selectNutrients(nutrients: Nutrients) {
        _selectedNutrients.value = nutrients
    }

    fun selectFeeds(feeds: List<Feed>) {
        _selectedFeeds.value = feeds
    }

    fun setResult(result: SimplexResult) {
        _result.value = result
    }
}