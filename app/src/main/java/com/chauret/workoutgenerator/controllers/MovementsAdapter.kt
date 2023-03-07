package com.chauret.workoutgenerator.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.model.movement.Movement

class MovementsAdapter(
    private val context: Context,
    private val movements: List<Movement>
): BaseAdapter(), ListAdapter {
    override fun getCount(): Int {
        return movements.size
    }

    override fun getItem(index: Int): Any {
        return movements[index]
    }

    override fun getItemId(index: Int): Long {
        return movements[index].id
    }

    override fun getView(index: Int, nullableView: View?, parent: ViewGroup?): View {
        val view = nullableView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.movement_list_item, null)
        val movement = movements[index]

        // Handle TextView and display string from your list
        val movementNameTextView = view.findViewById<TextView>(R.id.movementName)
        movementNameTextView.text = movement.name

        // Handle TextView and display string from your list
        val workoutTypesTextView = view.findViewById<TextView>(R.id.workoutTypes)
        workoutTypesTextView.text = movement.workoutTypes.joinToString(", ")

        view.setOnClickListener {
            val bundle = bundleOf("movement" to movement, "editable" to true, "deletable" to true)
            view.findNavController().navigate(
                R.id.action_navigation_movements_to_navigation_movement,
                bundle
            )
        }

        return view
    }
}