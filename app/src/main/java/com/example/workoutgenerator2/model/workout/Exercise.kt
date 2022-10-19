package com.example.workoutgenerator2.model.workout

import com.example.workoutgenerator2.model.movement.Movement
import java.util.UUID

data class Exercise (
    val guid: UUID,
    val movement: Movement,
    val sets: List<Int>
)