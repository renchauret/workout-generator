package com.chauret.workoutgenerator.storage

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.chauret.workoutgenerator.model.movement.WorkoutType
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID
import kotlin.random.Random

class WorkoutTypesDataStore {
    companion object {
        private val WORKOUT_TYPES_FILENAME = "workout_generator_workout_types"

        fun loadWorkoutTypes(activity: FragmentActivity): Set<WorkoutType> {
            try {
                val fis = activity.openFileInput(WORKOUT_TYPES_FILENAME)
                val ois = ObjectInputStream(fis)
                val workoutTypes: Set<WorkoutType> = ois.readObject() as Set<WorkoutType>
                fis.close()
                ois.close()
                return workoutTypes
            } catch (e: Exception) {
                println("workout_generator_workout_types file not found, creating new")
            }
            val workoutTypes = setOf(
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Chest"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Shoulders"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Back"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Legs"),
                WorkoutType(UUID.randomUUID(), Random.nextInt(0, Int.MAX_VALUE), "Arms")
            )
            saveWorkoutTypes(workoutTypes, activity)
            return workoutTypes
        }

        fun saveWorkoutTypes(workoutTypes: Set<WorkoutType>, activity: FragmentActivity) {
            try {
                val fos: FileOutputStream =
                    activity.openFileOutput(WORKOUT_TYPES_FILENAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(workoutTypes)
                oos.close()
                fos.close()
            } catch (e: java.lang.Exception) {
                println("Workout types file not found $e")
            }
        }
    }
}