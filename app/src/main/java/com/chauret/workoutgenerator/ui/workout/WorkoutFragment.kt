package com.chauret.workoutgenerator.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chauret.workoutgenerator.R
import com.chauret.workoutgenerator.controllers.ExercisesAdapter
import com.chauret.workoutgenerator.databinding.FragmentViewWorkoutBinding
import com.chauret.workoutgenerator.model.movement.WorkoutConfig
import com.chauret.workoutgenerator.model.workout.Workout
import com.chauret.workoutgenerator.model.workout.WorkoutFactory
import com.chauret.workoutgenerator.storage.MovementsDataStore
import com.chauret.workoutgenerator.storage.WorkoutsDataStore

//class WorkoutFragment(val workout: Workout) : Fragment() {
class WorkoutFragment() : Fragment() {

    private var _binding: FragmentViewWorkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var workout: Workout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val workoutViewModel =
            ViewModelProvider(this)[WorkoutViewModel::class.java]

        _binding = FragmentViewWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        workout = arguments?.get("workout") as Workout

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
        confirmButton.setOnClickListener {
            WorkoutsDataStore.saveWorkout(workout, requireActivity())
            findNavController().navigateUp()
        }
        val cancelButton: Button = binding.cancelButton
        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}