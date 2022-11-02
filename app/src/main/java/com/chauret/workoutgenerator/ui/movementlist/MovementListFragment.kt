package com.chauret.workoutgenerator.ui.movementlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chauret.workoutgenerator.controllers.MovementsAdapter
import com.chauret.workoutgenerator.databinding.FragmentMovementListBinding
import com.chauret.workoutgenerator.storage.MovementsDataStore

class MovementListFragment : Fragment() {

    private var _binding: FragmentMovementListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}