package com.chauret.workoutgenerator.ui.workout

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.controllers.ExercisesAdapter
import com.chauret.workoutgenerator.databinding.FragmentWorkoutBinding
import com.chauret.workoutgenerator.model.workout.Workout
import com.chauret.workoutgenerator.storage.WorkoutsDataStore


class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var workout: Workout
    private var editable: Boolean = false
    private var deletable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val workoutViewModel =
            ViewModelProvider(this)[WorkoutViewModel::class.java]

        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        workout = arguments?.get("workout") as Workout
        editable = arguments?.get("editable") as Boolean
        deletable = arguments?.get("deletable") as Boolean

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutTitle: TextView = binding.workoutTitle
        workoutTitle.text = workout.getDateTime()

        val workoutTypes: TextView = binding.workoutTypes
        workoutTypes.text = workout.config.workoutTypes.joinToString(", ")

        val exercisesList: ListView = binding.exercisesList
        val exercisesAdapter = this.context?.let { ExercisesAdapter(it, workout.exercises) }
        exercisesList.adapter = exercisesAdapter
        (exercisesAdapter as BaseAdapter).notifyDataSetChanged()

        val confirmButton: Button = binding.confirmButton
        val deleteButton: Button = binding.deleteButton
        val cancelButton: Button = binding.cancelButton
        if (editable) {
            confirmButton.setOnClickListener {
                WorkoutsDataStore.saveWorkout(workout, requireActivity())
                findNavController().navigateUp()
            }
        } else {
            confirmButton.visibility = View.GONE
        }
        if (deletable) {
            deleteButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage("Delete workout from history?")
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        R.string.yes
                    ) { _, _ ->
                        WorkoutsDataStore.deleteWorkout(workout.guid, requireActivity())
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.no, null).show()
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
}