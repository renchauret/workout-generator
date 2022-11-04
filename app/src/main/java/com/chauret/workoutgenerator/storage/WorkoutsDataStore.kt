package com.chauret.workoutgenerator.storage

import android.content.Context
import com.chauret.workoutgenerator.model.workout.Workout
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

class WorkoutsDataStore {
    companion object {
        private const val WORKOUTS_FILENAME = "workout_generator_workouts"

        fun loadWorkouts(context: Context): Set<Workout> {
            try {
                val fis = context.openFileInput(WORKOUTS_FILENAME)
                val ois = ObjectInputStream(fis)
                val workouts: Set<Workout> = ois.readObject() as Set<Workout>
                fis.close()
                ois.close()
                return workouts
            } catch (e: Exception) {
                println("workout_generator_workouts file not found, creating new")
            }
            val workouts = setOf<Workout>()
            saveWorkouts(workouts, context)
            return workouts
        }

        private fun saveWorkouts(workouts: Set<Workout>, context: Context) {
            try {
                val fos: FileOutputStream =
                    context.openFileOutput(WORKOUTS_FILENAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(workouts)
                oos.close()
                fos.close()
            } catch (e: java.lang.Exception) {
                println("Workouts file not found $e")
            }
        }

        fun saveWorkout(workout: Workout, context: Context) {
            val workouts = loadWorkouts(context)
            var found = false
            val newWorkouts = workouts.map {
                if (workout.guid == it.guid) {
                    found = true
                    workout
                } else it
            }
            val finalWorkouts: Set<Workout> = (if (found) newWorkouts else newWorkouts + workout).toSet()
            saveWorkouts(finalWorkouts, context)
        }

        fun deleteWorkout(workoutGuid: UUID, context: Context) {
            val workouts = loadWorkouts(context)
            val newWorkouts = workouts.filter {
                it.guid != workoutGuid
            }.toSet()
            saveWorkouts(newWorkouts, context)
        }
    }
}