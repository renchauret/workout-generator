package com.chauret.workoutgenerator.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.databinding.FragmentGenerateWorkoutBinding
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.WorkoutConfig
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.model.workout.WorkoutFactory
import com.chauret.workoutgenerator.storage.MovementsDataStore
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider

class GenerateWorkoutFragment : Fragment() {

    private var _binding: FragmentGenerateWorkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var workoutTypes: Set<WorkoutType> = setOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val generateWorkoutViewModel =
            ViewModelProvider(this)[GenerateWorkoutViewModel::class.java]

        _binding = FragmentGenerateWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.generateTitle
        generateWorkoutViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it
        }

        workoutTypes = loadWorkoutTypes()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val selectWorkoutTypesChipGroup: ChipGroup = view.findViewById(R.id.selectWorkoutTypesChipGroup)
        val selectWorkoutTypesChipGroup: ChipGroup = binding.selectWorkoutTypesChipGroup
        workoutTypes.forEach {
            val workoutTypeChip = Chip(this.context)
            workoutTypeChip.id = it.id
            workoutTypeChip.text = it.name
            workoutTypeChip.isCheckable = true
            workoutTypeChip.checkedIcon = null
            workoutTypeChip.chipBackgroundColor
            selectWorkoutTypesChipGroup.addView(workoutTypeChip)
        }

        val selectExercisesRangeSlider: RangeSlider = binding.selectExercisesRangeSlider
        selectExercisesRangeSlider.values = listOf(3f, 5f)

        val generateButton: Button = binding.generateButton
        generateButton.setOnClickListener {
            val workoutConfig = WorkoutConfig(
                workoutTypes = selectWorkoutTypesChipGroup.checkedChipIds.map { chipId -> workoutTypes.find { it.id == chipId }!! }.toSet(),
                minExercises = selectExercisesRangeSlider.values[0].toInt(),
                maxExercises = selectExercisesRangeSlider.values[1].toInt()
            )
            val workout = WorkoutFactory.createWorkout(workoutConfig, MovementsDataStore.loadMovements(requireActivity()).toList())
            val bundle = bundleOf("workout" to workout, "editable" to true, "deletable" to false)
            findNavController().navigate(
                R.id.action_navigation_generate_to_navigation_workout,
                bundle
            )
//            val workoutFragment = WorkoutFragment(workout)
//            (it.context as FragmentActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.generateWorkoutFragment, workoutFragment)
//                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadWorkoutTypes(): Set<WorkoutType> {
        return WorkoutTypesDataStore.loadWorkoutTypes(requireActivity())
    }

    private fun loadMovements(): Set<Movement> {
        return MovementsDataStore.loadMovements(requireActivity())
    }
}