package com.jery.feedformulation.ui.tmrmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TmrmakerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to SmartTools"
    }
    val text: LiveData<String> = _text
}