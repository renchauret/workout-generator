package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.WorkoutConfig
import java.util.UUID
import kotlin.random.Random

class WorkoutFactory {
    companion object {
        private val random: Random = Random(System.currentTimeMillis())

        fun createWorkout(config: WorkoutConfig, possibleMovements: List<Movement>): Workout {
            return Workout(
                guid = UUID.randomUUID(),
                id = random.nextLong(0, Long.MAX_VALUE),
                config = config,
                exercises = createExercises(config, possibleMovements),
                timestampMillis = System.currentTimeMillis()
            )
        }

        private fun createExercises(config: WorkoutConfig, possibleMovements: List<Movement>): List<Exercise> {
            val legalMovements: MutableSet<Movement> = possibleMovements.filter {
                it.workoutTypes.intersect(config.workoutTypes).isNotEmpty()
            }.toMutableSet()
            val numExercises = (minOf(config.minExercises, legalMovements.size)..minOf(config.maxExercises, legalMovements.size)).random(random)
            return (0 until numExercises).map {
                val weightedIndices = mutableListOf<Int>()
                legalMovements.forEachIndexed { index, movement ->
                    // favorite movements are 50% more likely to be picked
                    if (movement.favorite == true) weightedIndices.add(index)
                    weightedIndices.add(index)
                    weightedIndices.add(index)
                }
                val index = weightedIndices.random(random)
                val movement = legalMovements.elementAt(index)
                legalMovements.remove(movement)
                ExerciseFactory.createExercise(movement)
            }
        }
    }
}
