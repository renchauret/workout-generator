package com.chauret.workoutgenerator.storage

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.chauret.workoutgenerator.model.workout.Workout
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

class WorkoutsDataStore {
    companion object {
        private val WORKOUTS_FILENAME = "workout_generator_workouts"

        fun loadWorkouts(activity: FragmentActivity): Set<Workout> {
            try {
                val fis = activity.openFileInput(WORKOUTS_FILENAME)
                val ois = ObjectInputStream(fis)
                val workouts: Set<Workout> = ois.readObject() as Set<Workout>
                fis.close()
                ois.close()
                return workouts
            } catch (e: Exception) {
                println("workout_generator_workouts file not found, creating new")
            }
            val workouts = setOf<Workout>()
            saveWorkouts(workouts, activity)
            return workouts
        }

        private fun saveWorkouts(workouts: Set<Workout>, activity: FragmentActivity) {
            try {
                val fos: FileOutputStream =
                    activity.openFileOutput(WORKOUTS_FILENAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(workouts)
                oos.close()
                fos.close()
            } catch (e: java.lang.Exception) {
                println("Workouts file not found $e")
            }
        }

        fun saveWorkout(workout: Workout, activity: FragmentActivity) {
            val workouts = loadWorkouts(activity)
            var found = false
            val newWorkouts = workouts.map {
                if (workout.guid == it.guid) {
                    found = true
                    workout
                } else it
            }
            val finalWorkouts: Set<Workout> = (if (found) newWorkouts else newWorkouts + workout).toSet()
            saveWorkouts(finalWorkouts, activity)
        }

        fun deleteWorkout(workoutGuid: UUID, activity: FragmentActivity) {
            val workouts = loadWorkouts(activity)
            val newWorkouts = workouts.filter {
                it.guid != workoutGuid
            }.toSet()
            saveWorkouts(newWorkouts, activity)
        }
    }
}