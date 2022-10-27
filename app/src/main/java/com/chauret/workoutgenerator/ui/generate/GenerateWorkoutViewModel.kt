package com.chauret.workoutgenerator.ui.generate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateWorkoutViewModel : ViewModel() {

    private val _titleText = MutableLiveData<String>().apply {
        value = "Generate Workout"
    }
    val titleText: LiveData<String> = _titleText
}