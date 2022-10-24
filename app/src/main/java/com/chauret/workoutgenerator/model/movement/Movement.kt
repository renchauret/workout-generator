package com.chauret.workoutgenerator.model.movement

import java.util.UUID

data class Movement (
    val guid: UUID,
    val name: String,
    val workoutTypes: List<WorkoutType>,
    val minSets: Int,
    val maxSets: Int,
    val minReps: Int,
    val maxReps: Int,
    val repUnit: RepUnit,
    val setStructures: List<SetStructure>
)