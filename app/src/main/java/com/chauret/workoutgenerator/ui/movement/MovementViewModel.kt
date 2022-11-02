package com.chauret.workoutgenerator.ui.movement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovementViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "View Workout"
    }
    val text: LiveData<String> = _text
}