package com.chauret.workoutgenerator.model.movement

import java.util.UUID

data class WorkoutType (
    val guid: UUID,
    // for Android UI
    val id: Int,
    val name: String
)