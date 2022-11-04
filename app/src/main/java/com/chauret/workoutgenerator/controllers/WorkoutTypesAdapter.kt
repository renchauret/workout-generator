package com.chauret.workoutgenerator.controllers

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore

class WorkoutTypesAdapter(
    private val context: Context,
    private val workoutTypes: List<WorkoutType>
): BaseAdapter(), ListAdapter {
    override fun getCount(): Int {
        return workoutTypes.size
    }

    override fun getItem(index: Int): Any {
        return workoutTypes[index]
    }

    override fun getItemId(index: Int): Long {
        return workoutTypes[index].id.toLong()
    }

    override fun getView(index: Int, nullableView: View?, parent: ViewGroup?): View {
        val view = nullableView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.workout_type_list_item, null)
        val workoutType = workoutTypes[index]

        // Handle TextView and display string from your list
        val workoutTypeNameTextView = view.findViewById<TextView>(R.id.workoutTypeName)
        workoutTypeNameTextView.text = workoutType.name

        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Delete \"${workoutType.name}\"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    WorkoutTypesDataStore.deleteWorkoutType(workoutType.guid, context)
                }
                .setNegativeButton(android.R.string.no, null).show()
        }

        return view
    }
}