package com.chauret.workoutgenerator.ui.movement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.databinding.FragmentMovementBinding
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.SetStructure
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.storage.MovementsDataStore
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider

class MovementFragment : Fragment() {

    private var _binding: FragmentMovementBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var movement: Movement
    private lateinit var workoutTypes: Set<WorkoutType>
    private var editable: Boolean = false
    private var deletable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val movementViewModel =
            ViewModelProvider(this)[MovementViewModel::class.java]

        _binding = FragmentMovementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        movement = arguments?.get("movement") as Movement
        editable = arguments?.get("editable") as Boolean
        deletable = arguments?.get("deletable") as Boolean
        workoutTypes = loadWorkoutTypes()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movementName: TextView = binding.movementName
        movementName.text = movement.name

        val selectWorkoutTypesChipGroup: ChipGroup = binding.selectWorkoutTypesChipGroup
        workoutTypes.forEach {
            val workoutTypeChip = Chip(this.context)
            workoutTypeChip.id = it.id
            workoutTypeChip.text = it.name
            workoutTypeChip.isCheckable = true
            workoutTypeChip.checkedIcon = null
            workoutTypeChip.chipBackgroundColor
            workoutTypeChip.isChecked = movement.workoutTypes.contains(it)
            selectWorkoutTypesChipGroup.addView(workoutTypeChip)
        }
        selectWorkoutTypesChipGroup.isSelectionRequired = true

        val selectSetStructuresChipGroup: ChipGroup = binding.selectSetStructuresChipGroup
        SetStructure.values().forEachIndexed { index, setStructure ->
            val setStructureChip = Chip(this.context)
            setStructureChip.id = index
            setStructureChip.text = setStructure.name[0] + setStructure.name.substring(1).lowercase();
            setStructureChip.isCheckable = true
            setStructureChip.checkedIcon = null
            setStructureChip.chipBackgroundColor
            setStructureChip.isChecked = movement.setStructures.contains(setStructure)
            selectSetStructuresChipGroup.addView(setStructureChip)
        }
        selectSetStructuresChipGroup.isSelectionRequired = true

        val setCountRangeSlider: RangeSlider = binding.setCountRangeSlider
        setCountRangeSlider.values = listOf(movement.minSets.toFloat(), movement.maxSets.toFloat())

        val repCountRangeSlider: RangeSlider = binding.repCountRangeSlider
        repCountRangeSlider.values = listOf(movement.minReps.toFloat(), movement.maxReps.toFloat())

        val confirmButton: Button = binding.confirmButton
        val deleteButton: Button = binding.deleteButton
        val cancelButton: Button = binding.cancelButton
        if (editable) {
            confirmButton.setOnClickListener {
                val movement = movement.copy (
                    name = movementName.text.toString(),
                    workoutTypes = selectWorkoutTypesChipGroup.checkedChipIds.map { chipId -> workoutTypes.find { it.id == chipId }!! }.toSet(),
                    setStructures = selectSetStructuresChipGroup.checkedChipIds.map { chipId -> SetStructure.values()[chipId] }.toSet(),
                    minSets = setCountRangeSlider.values[0].toInt(),
                    maxSets = setCountRangeSlider.values[1].toInt(),
                    minReps = repCountRangeSlider.values[0].toInt(),
                    maxReps = repCountRangeSlider.values[1].toInt()
                )
                MovementsDataStore.saveMovement(movement, requireActivity())
                findNavController().navigateUp()
            }
        } else {
            confirmButton.visibility = View.GONE
        }
        if (deletable) {
            deleteButton.setOnClickListener {
                MovementsDataStore.deleteMovement(movement.guid, requireActivity())
                findNavController().navigateUp()
            }
        } else {
            deleteButton.visibility = View.GONE
        }
        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadWorkoutTypes(): Set<WorkoutType> {
        return WorkoutTypesDataStore.loadWorkoutTypes(requireActivity())
    }
}