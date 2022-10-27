package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.Movement
import java.util.UUID

data class Exercise (
    val guid: UUID,
    // for Android list adapters
    val id: Long,
    val movement: Movement,
    val sets: List<Int>
)
