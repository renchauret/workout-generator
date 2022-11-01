package com.chauret.workoutgenerator.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.chauret.workoutgenerator.R
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

        view.setOnClickListener {
            val bundle = bundleOf("workout" to workout, "editable" to false, "deletable" to true)
            view.findNavController().navigate(
                R.id.action_navigation_history_to_navigation_workout,
                bundle
            )
        }

        return view
    }
}