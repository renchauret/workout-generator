package com.chauret.workoutgenerator.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.databinding.FragmentGenerateWorkoutBinding
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import java.io.ObjectInputStream
import java.util.*

class GenerateWorkoutFragment : Fragment() {

    private var _binding: FragmentGenerateWorkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val WORKOUT_TYPES_FILENAME = "workout_generator_workout_types"

    private var workoutTypes: Set<WorkoutType> = setOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(GenerateWorkoutViewModel::class.java)

        _binding = FragmentGenerateWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.generateTitle
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        workoutTypes = loadWorkoutTypes()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectWorkoutTypesChipGroup: ChipGroup = view.findViewById(R.id.selectWorkoutTypesChipGroup)
        workoutTypes.forEach {
            val workoutTypeChip = Chip(this.context)
            workoutTypeChip.text = it.name
            workoutTypeChip.isCheckable = true
            workoutTypeChip.checkedIcon = null
            workoutTypeChip.chipBackgroundColor
            selectWorkoutTypesChipGroup.addView(workoutTypeChip)
        }

        val selectExercisesRangeSlider: RangeSlider = view.findViewById(R.id.selectExercisesRangeSlider)
        selectExercisesRangeSlider.values = listOf(3f, 5f)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadWorkoutTypes(): Set<WorkoutType> {
        try {
            val fis = requireActivity().openFileInput(WORKOUT_TYPES_FILENAME)
            val ois = ObjectInputStream(fis)
            val workoutTypes: Set<WorkoutType> = ois.readObject() as Set<WorkoutType>
            fis.close()
            ois.close()
            return workoutTypes
        } catch (e: Exception) {
            println("workout_generator_workout_types file not found, creating new")
        }
        return setOf(
            WorkoutType(UUID.randomUUID(), "Chest"),
            WorkoutType(UUID.randomUUID(), "Shoulders"),
            WorkoutType(UUID.randomUUID(), "Back"),
            WorkoutType(UUID.randomUUID(), "Legs"),
            WorkoutType(UUID.randomUUID(), "Arms")
        )
    }
}