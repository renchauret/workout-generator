package com.chauret.workoutgenerator.ui.movementlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.controllers.MovementsAdapter
import com.chauret.workoutgenerator.databinding.FragmentMovementListBinding
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.RepUnit
import com.chauret.workoutgenerator.model.movement.SetStructure
import com.chauret.workoutgenerator.storage.MovementsDataStore
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore
import java.util.UUID
import kotlin.random.Random

class MovementListFragment : Fragment() {

    private var _binding: FragmentMovementListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[MovementListViewModel::class.java]

        _binding = FragmentMovementListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movements = MovementsDataStore.loadMovements(requireActivity())
        val movementsList: ListView = binding.movementsList
        val movementsAdapter = this.context?.let { MovementsAdapter(it, movements.toList()) }
        movementsList.adapter = movementsAdapter
        (movementsAdapter as BaseAdapter).notifyDataSetChanged()

        val addButton: Button = binding.addButton
        addButton.setOnClickListener {
            val workoutTypes = WorkoutTypesDataStore.loadWorkoutTypes(requireActivity())
            val movement =  Movement(
                guid = UUID.randomUUID(),
                id = Random.nextLong(0, Long.MAX_VALUE),
                name = "Exercise name",
                workoutTypes = setOf(
                    workoutTypes.first()
                ),
                minSets = 3,
                maxSets = 8,
                minReps = 1,
                maxReps = 8,
                repUnit = RepUnit.REPS,
                setStructures = setOf(
                    SetStructure.FLAT,
                    SetStructure.DESCENDING,
                    SetStructure.PYRAMID
                )
            )
            val bundle = bundleOf("movement" to movement, "editable" to true, "deletable" to false)
            view.findNavController().navigate(
                R.id.action_navigation_movements_to_navigation_movement,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}