package com.chauret.workoutgenerator.storage

import android.content.Context
import com.chauret.workoutgenerator.model.Entity
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.WorkoutType
import com.chauret.workoutgenerator.model.workout.Workout
import kotlin.reflect.KClass

class DataStoreFactory {
    companion object {
        private val dataStoreFileNameMap: Map<KClass<*>, String> = mapOf(
            Pair(Workout::class, "workout_generator_workouts"),
            Pair(Movement::class, "workout_generator_movements"),
            Pair(WorkoutType::class, "workout_generator_workout_types")
        )

        fun <T: Entity> create(context: Context, clazz: KClass<T>): DataStore<T> {
            dataStoreFileNameMap[clazz]?.let { return FileDataStore(it, context) }
            throw NullPointerException("Attempted to create file datastore with undefined filename")
        }
    }
}