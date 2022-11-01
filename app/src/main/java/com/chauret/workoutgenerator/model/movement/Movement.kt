package com.chauret.workoutgenerator.model.movement

import java.io.Serializable
import java.util.UUID

data class Movement (
    val guid: UUID,
    // for Android UI
    val id: Long,
    val name: String,
    val workoutTypes: Set<WorkoutType>,
    val minSets: Int,
    val maxSets: Int,
    val minReps: Int,
    val maxReps: Int,
    val repUnit: RepUnit,
    val setStructures: Set<SetStructure>
): Serializable
