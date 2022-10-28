package com.chauret.workoutgenerator.model.movement

import java.util.UUID
import java.io.Serializable

data class WorkoutType (
    val guid: UUID,
    // for Android UI
    val id: Int,
    val name: String
): Serializable {
    override fun toString(): String {
        return name
    }
}
