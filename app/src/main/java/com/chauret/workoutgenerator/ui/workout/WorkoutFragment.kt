package com.chauret.workoutgenerator.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chauret.workoutgenerator.controllers.ExercisesAdapter
import com.chauret.workoutgenerator.databinding.FragmentViewWorkoutBinding
import com.chauret.workoutgenerator.model.workout.Workout

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

//        val textView: TextView = binding.
//        workoutViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val workoutTitle: TextView = binding.workoutTitle // requireView().findViewById(R.id.workoutTitle)
        workoutTitle.text = workout.timestampMillis.toString()

        val workoutTypes: TextView = binding.workoutTypes // requireView().findViewById(R.id.workoutTypes)
        workoutTypes.text = workout.config.workoutTypes.joinToString(", ")

        val exercisesList: ListView = binding.exercisesList // requireView().findViewById(R.id.exercisesList)
        val exercisesAdapter = this.context?.let { ExercisesAdapter(it, workout.exercises) }
        exercisesList.adapter = exercisesAdapter
        (exercisesAdapter as BaseAdapter).notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}