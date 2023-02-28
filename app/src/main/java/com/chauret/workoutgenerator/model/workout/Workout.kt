package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.Entity
import com.chauret.workoutgenerator.model.movement.WorkoutConfig
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Workout (
    override val guid: UUID,
    // for Android list adapters
    val id: Long,
    val config: WorkoutConfig,
    val exercises: List<Exercise>,
    val timestampMillis: Long
): Serializable, Entity {
    fun getDateTime(): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)
            val netDate = Date(timestampMillis)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}
