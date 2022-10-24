package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.WorkoutType
import java.util.UUID

data class Workout (
    val guid: UUID,
    val workoutTypes: List<WorkoutType>,
    val exercises: List<Exercise>
)