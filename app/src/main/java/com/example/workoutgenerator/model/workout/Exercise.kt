package com.example.workoutgenerator.model.workout

import com.example.workoutgenerator.model.movement.Movement
import java.util.UUID

data class Exercise (
    val guid: UUID,
    val movement: Movement,
    val sets: List<Int>
)