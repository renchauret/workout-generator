package com.example.workoutgenerator.model.workout

import com.example.workoutgenerator.model.movement.WorkoutType
import java.util.UUID

data class Workout (
    val guid: UUID,
    val workoutTypes: List<WorkoutType>,
    val exercises: List<Exercise>
)