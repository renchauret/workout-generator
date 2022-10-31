package com.chauret.workoutgenerator.model.movement

data class WorkoutConfig(
    val workoutTypes: Set<WorkoutType>,
    val minExercises: Int,
    val maxExercises: Int
): java.io.Serializable

