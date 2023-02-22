package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.RepUnit
import com.chauret.workoutgenerator.model.movement.SetStructure
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.random.Random

class ExerciseFactory {
    companion object {
        fun createExercise(movement: Movement): Exercise {
            return Exercise(
                guid = UUID.randomUUID(),
                id = Random.nextLong(0, Long.MAX_VALUE),
                movement = movement,
                sets = createSets(movement)
            )
        }

        private fun createSets(movement: Movement): List<Int> {
            val numSets = (movement.minSets..movement.maxSets).random()
            val step = if (movement.repUnit == RepUnit.SECONDS) 10 else 1
            return when (movement.setStructures.random()) {
                SetStructure.PYRAMID -> createPyramidSets(
                    numSets,
                    movement.minReps,
                    movement.maxReps,
                    step
                )
                SetStructure.RANDOM -> createRandomSets(
                    numSets,
                    movement.minReps,
                    movement.maxReps,
                    step
                )
                SetStructure.DESCENDING -> createDescendingSets(
                    numSets,
                    movement.minReps,
                    movement.maxReps,
                    step
                )
                SetStructure.FLAT -> createFlatSets(
                    numSets,
                    movement.minReps,
                    movement.maxReps,
                    step
                )
            }
        }

        private fun createFlatSets(numSets: Int, minReps: Int, maxReps: Int, step: Int = 1): List<Int> {
            val reps = (minReps..maxReps step step).toList().random()
            return List(numSets) { reps }
        }

        private fun createRandomSets(numSets: Int, minReps: Int, maxReps: Int, step: Int = 1): List<Int> {
            return List(numSets) { (minReps..maxReps step step).toList().random() }
        }

        private fun createDescendingSets(numSets: Int, minReps: Int, maxReps: Int, step: Int = 1): List<Int> {
            val finalStep: Double = (maxReps - minReps).toDouble() / numSets
            return List(numSets) {
                val thisMax: Int = ((maxReps - (finalStep * it)) / step).roundToInt() * step
                val thisMin: Int =
                    if (thisMax == minReps) minReps else ((maxReps - (finalStep * (it + 1))) / step).roundToInt() * step
                (thisMin..thisMax step step).toList().random()
            }
        }

        private fun createPyramidSets(numSets: Int, minReps: Int, maxReps: Int, step: Int = 1): List<Int> {
            val finalStep: Double =
                (maxReps - minReps).toDouble() / (numSets.toDouble() / 2).roundToInt()
            return List(numSets) {
                val halfIndex = numSets / 2
                val isFrontHalf = it <= halfIndex
                val stepMultiplier = if (isFrontHalf) it else halfIndex - (it - halfIndex)
                val thisMax: Int = ((maxReps - (finalStep * stepMultiplier)) / step).roundToInt() * step
                val thisMin: Int =
                    if (thisMax == minReps) minReps else ((maxReps - (finalStep * (stepMultiplier + 1))) / step).roundToInt() * step
                (thisMin..thisMax step step).toList().random()
            }
        }
    }
}
