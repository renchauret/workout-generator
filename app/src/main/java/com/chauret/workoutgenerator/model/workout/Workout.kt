package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.WorkoutConfig
import java.util.UUID

data class Workout (
    val guid: UUID,
    val config: WorkoutConfig,
    val exercises: List<Exercise>,
    val timestampMillis: Long
)
