package com.chauret.workoutgenerator.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.model.workout.Exercise
import com.chauret.workoutgenerator.model.workout.Workout

class WorkoutsAdapter(
    private val context: Context,
    private val workouts: List<Workout>
): BaseAdapter(), ListAdapter {
    override fun getCount(): Int {
        return workouts.size
    }

    override fun getItem(index: Int): Any {
        return workouts[index]
    }

    override fun getItemId(index: Int): Long {
        return workouts[index].id
    }

    override fun getView(index: Int, nullableView: View?, parent: ViewGroup?): View {
        val view = nullableView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.workouts_list_item, null)
        val workout = workouts[index]

        // Handle TextView and display string from your list
        val workoutDateTextView = view.findViewById<TextView>(R.id.workoutDate)
        workoutDateTextView.text = workout.getDateTime()

        // Handle TextView and display string from your list
        val workoutTypesTextView = view.findViewById<TextView>(R.id.workoutTypes)
        workoutTypesTextView.text = workout.config.workoutTypes.joinToString(", ")

        return view
    }

    private fun getSetsText(exercise: Exercise): String {
        var isFlat = true
        val flatRepCount = exercise.sets[0]
        val enumeratedSetsText: String = exercise.sets.map {
            if (isFlat && it != flatRepCount) isFlat = false
            it
        }.joinToString(", ")
        return if (isFlat) "${exercise.sets.size} x $flatRepCount" else enumeratedSetsText
    }
}