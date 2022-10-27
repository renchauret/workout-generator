package com.chauret.workoutgenerator.storage

import androidx.fragment.app.FragmentActivity
import com.chauret.workoutgenerator.model.movement.WorkoutType
import java.io.ObjectInputStream
import java.util.*
import kotlin.random.Random

class WorkoutTypesDataStore {
    companion object {
        private val WORKOUT_TYPES_FILENAME = "workout_generator_workout_types"

        fun loadWorkoutTypes(fragmentActivity: FragmentActivity): Set<WorkoutType> {
            try {
                val fis = fragmentActivity.openFileInput(WORKOUT_TYPES_FILENAME)
                val ois = ObjectInputStream(fis)
                val workoutTypes: Set<WorkoutType> = ois.readObject() as Set<WorkoutType>
                fis.close()
                ois.close()
                return workoutTypes
            } catch (e: Exception) {
                println("workout_generator_workout_types file not found, creating new")
            }
            return setOf(
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Chest"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Shoulders"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Back"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Legs"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Arms")
            )
        }
    }
}