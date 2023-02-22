package com.chauret.workoutgenerator.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.model.movement.RepUnit
import com.chauret.workoutgenerator.model.workout.Exercise

class ExercisesAdapter(
    private val context: Context,
    private val exercises: List<Exercise>
): BaseAdapter(), ListAdapter {
    override fun getCount(): Int {
        return exercises.size
    }

    override fun getItem(index: Int): Any {
        return exercises[index]
    }

    override fun getItemId(index: Int): Long {
        return exercises[index].id
    }

    override fun getView(index: Int, nullableView: View?, parent: ViewGroup?): View {
        val view = nullableView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.exercise_list_item, null)
        val exercise = exercises[index]

        // Handle TextView and display string from your list
        val exerciseNameTextView = view.findViewById<TextView>(R.id.exerciseName)
        exerciseNameTextView.text = exercise.movement.name

        // Handle TextView and display string from your list
        val setsTextView = view.findViewById<TextView>(R.id.sets)
        setsTextView.text = getSetsText(exercise)

        return view
    }

    private fun getSetsText(exercise: Exercise): String {
        var isFlat = true
        val flatRepCount = exercise.sets[0]
        val enumeratedSetsText: String = exercise.sets.map {
            if (isFlat && it != flatRepCount) isFlat = false
            it
        }.joinToString(", ")
        val setsString = if (isFlat) "${exercise.sets.size} x $flatRepCount" else enumeratedSetsText
        val repUnit = exercise.movement.repUnit
        return if (repUnit == RepUnit.SECONDS) "$setsString sec" else setsString
    }
}