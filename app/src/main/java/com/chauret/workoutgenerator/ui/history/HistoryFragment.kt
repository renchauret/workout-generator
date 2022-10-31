package com.chauret.workoutgenerator.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chauret.workoutgenerator.controllers.WorkoutsAdapter
import com.chauret.workoutgenerator.databinding.FragmentHistoryBinding
import com.chauret.workoutgenerator.storage.WorkoutsDataStore

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workouts = WorkoutsDataStore.loadWorkouts(requireActivity())
        val workoutsList: ListView = binding.workoutsList
        val workoutsAdapter = this.context?.let { WorkoutsAdapter(it, workouts.toList()) }
        workoutsList.adapter = workoutsAdapter
        (workoutsAdapter as BaseAdapter).notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}