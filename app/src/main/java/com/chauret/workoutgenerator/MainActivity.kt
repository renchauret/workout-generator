package com.chauret.workoutgenerator

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chauret.workoutgenerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_generate,
                R.id.navigation_history,
                R.id.navigation_movements,
//                R.id.navigation_workout,
//                R.id.navigation_movement,
//                R.id.navigation_workout_types
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

       navView.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it,findNavController(R.id.nav_host_fragment_activity_main))
            findNavController(R.id.nav_host_fragment_activity_main).popBackStack(it.itemId, inclusive = false)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.popBackStack() || super.onSupportNavigateUp()
    }
}
