package com.chauret.workoutgenerator.ui.workouttypelist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.controllers.WorkoutTypesAdapter
import com.chauret.workoutgenerator.databinding.FragmentWorkoutTypeListBinding
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.storage.WorkoutTypesDataStore
import java.util.*
import kotlin.random.Random

class WorkoutTypeListFragment : Fragment() {

    private var _binding: FragmentWorkoutTypeListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var workoutTypes: MutableList<WorkoutType>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[WorkoutTypeListViewModel::class.java]

        _binding = FragmentWorkoutTypeListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutTypes = WorkoutTypesDataStore.loadWorkoutTypes(requireContext()).toMutableList()
        val workoutTypesList: ListView = binding.workoutTypesList
        val workoutTypesAdapter = WorkoutTypesAdapter(requireContext(), workoutTypes)
        workoutTypesList.adapter = workoutTypesAdapter
        (workoutTypesList.adapter as BaseAdapter).notifyDataSetChanged()

        val cancelButton: Button = binding.cancelButton
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val addButton: Button = binding.addButton
        addButton.setOnClickListener {
            val editText = EditText(requireContext())
            editText.inputType = InputType.TYPE_CLASS_TEXT
            AlertDialog.Builder(context)
                .setMessage("Create workout type")
                .setView(editText)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    val name = editText.text.toString()
                    if (name.isNotEmpty()) {
                        val workoutType = WorkoutType(
                            guid = UUID.randomUUID(),
                            id = Random.nextInt(),
                            name = editText.text.toString()
                        )
                        WorkoutTypesDataStore.saveWorkoutType(
                            workoutType,
                            requireContext()
                        )
                        workoutTypes.add(workoutType)
                        (workoutTypesList.adapter as BaseAdapter).notifyDataSetChanged()
                    }
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}