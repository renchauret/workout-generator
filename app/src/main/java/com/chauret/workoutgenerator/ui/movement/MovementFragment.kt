package com.chauret.workoutgenerator.ui.movement

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.databinding.FragmentMovementBinding
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.RepUnit
import com.chauret.workoutgenerator.model.movement.SetStructure
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.storage.MovementsDataStore
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import kotlin.random.Random

class MovementFragment : Fragment() {

    private var _binding: FragmentMovementBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
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
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> workoutTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list_dark)
                Configuration.UI_MODE_NIGHT_NO -> workoutTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list)
            }
            workoutTypeChip.isChecked = movement.workoutTypes.contains(it)
            selectWorkoutTypesChipGroup.addView(workoutTypeChip)
        }
        val addWorkoutTypeChip = Chip(this.context)
        addWorkoutTypeChip.id = Random(System.currentTimeMillis()).nextInt(0, Int.MAX_VALUE)
        addWorkoutTypeChip.text = "+ / -"
        addWorkoutTypeChip.isCheckable = false
        addWorkoutTypeChip.checkedIcon = null
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> addWorkoutTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                R.color.chip_state_list_dark)
            Configuration.UI_MODE_NIGHT_NO -> addWorkoutTypeChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                R.color.chip_state_list)
        }
        addWorkoutTypeChip.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_movement_to_navigation_workout_types
            )
        }
        selectWorkoutTypesChipGroup.addView(addWorkoutTypeChip)
        selectWorkoutTypesChipGroup.isSelectionRequired = true

        val selectSetStructuresChipGroup: ChipGroup = binding.selectSetStructuresChipGroup
        SetStructure.values().forEachIndexed { index, setStructure ->
            val setStructureChip = Chip(this.context)
            setStructureChip.id = index
            setStructureChip.text = setStructure.name[0] + setStructure.name.substring(1).lowercase()
            setStructureChip.isCheckable = true
            setStructureChip.checkedIcon = null
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> setStructureChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list_dark)
                Configuration.UI_MODE_NIGHT_NO -> setStructureChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list)
            }
            setStructureChip.isChecked = movement.setStructures.contains(setStructure)
            selectSetStructuresChipGroup.addView(setStructureChip)
        }
        selectSetStructuresChipGroup.isSelectionRequired = true

        val setCountRangeSlider: RangeSlider = binding.setCountRangeSlider
        setCountRangeSlider.values = listOf(movement.minSets.toFloat(), movement.maxSets.toFloat())

        val repUnitChipGroup: ChipGroup = binding.repUnitChipGroup
        RepUnit.values().forEachIndexed { index, repUnit ->
            val repUnitChip = Chip(this.context)
            repUnitChip.id = index
            repUnitChip.text = repUnit.name[0] + repUnit.name.substring(1).lowercase()
            repUnitChip.isCheckable = true
            repUnitChip.checkedIcon = null
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> repUnitChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list_dark)
                Configuration.UI_MODE_NIGHT_NO -> repUnitChip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(),
                    R.color.chip_state_list)
            }
            repUnitChip.isChecked = movement.repUnit == repUnit
            repUnitChipGroup.addView(repUnitChip)
        }
        repUnitChipGroup.isSelectionRequired = true
        val selectedRepUnit = RepUnit.values()[repUnitChipGroup.checkedChipId]

        val repCountRangeSliderText: TextView = binding.repCountRangeSliderText
        repCountRangeSliderText.text = "Min and Max " + selectedRepUnit.name[0] + selectedRepUnit.name.substring(1).lowercase()

        val repCountRangeSlider: RangeSlider = binding.repCountRangeSlider
        if (selectedRepUnit == RepUnit.SECONDS) {
            repCountRangeSlider.valueFrom = 10F
            repCountRangeSlider.valueTo = 200F
            repCountRangeSlider.stepSize = 10F
        } else {
            repCountRangeSlider.valueFrom = 1F
            repCountRangeSlider.valueTo = 20F
            repCountRangeSlider.stepSize = 1F
        }
        repCountRangeSlider.values = listOf(movement.minReps.toFloat(), movement.maxReps.toFloat())

        repUnitChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val repUnit = RepUnit.values()[group.checkedChipId]
            repCountRangeSliderText.text = "Min and Max " + repUnit.name[0] + repUnit.name.substring(1).lowercase()

            val currValues = repCountRangeSlider.values
            repCountRangeSlider.values = mutableListOf(10F, 10F)
            if (repUnit == RepUnit.SECONDS) {
                repCountRangeSlider.valueFrom = 10F
                repCountRangeSlider.valueTo = 200F
                repCountRangeSlider.stepSize = 10F
                repCountRangeSlider.values = currValues.map { it * 10 }
            } else {
                repCountRangeSlider.valueFrom = 1F
                repCountRangeSlider.valueTo = 20F
                repCountRangeSlider.stepSize = 1F
                repCountRangeSlider.values = currValues.map { it / 10 }
            }
        }

        val favoriteCheckbox: CheckBox = binding.favoriteCheckbox
        favoriteCheckbox.isChecked = movement.favorite ?: false

        val confirmButton: Button = binding.confirmButton
        val deleteButton: Button = binding.deleteButton
        val cancelButton: Button = binding.cancelButton
        if (editable) {
            confirmButton.setOnClickListener {
                val movement = movement.copy (
                    name = movementName.text.toString(),
                    workoutTypes = selectWorkoutTypesChipGroup.checkedChipIds.map { chipId -> workoutTypes.find { it.id == chipId }!! }.toSet(),
                    setStructures = selectSetStructuresChipGroup.checkedChipIds.map { chipId -> SetStructure.values()[chipId] }.toSet(),
                    repUnit = RepUnit.values()[repUnitChipGroup.checkedChipId],
                    minSets = setCountRangeSlider.values[0].toInt(),
                    maxSets = setCountRangeSlider.values[1].toInt(),
                    minReps = repCountRangeSlider.values[0].toInt(),
                    maxReps = repCountRangeSlider.values[1].toInt(),
                    favorite = favoriteCheckbox.isChecked
                )
                MovementsDataStore.saveMovement(movement, requireActivity())
                findNavController().popBackStack()
            }
        } else {
            confirmButton.visibility = View.GONE
        }
        if (deletable) {
            deleteButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage("Delete \"${movement.name}\"?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes
                    ) { _, _ ->
                        MovementsDataStore.deleteMovement(movement.guid, requireActivity())
                        findNavController().popBackStack()
                    }
                    .setNegativeButton(android.R.string.no, null).show()
            }
        } else {
            deleteButton.visibility = View.GONE
        }
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
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