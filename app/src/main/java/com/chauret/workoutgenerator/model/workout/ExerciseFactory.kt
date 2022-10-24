package com.chauret.workoutgenerator.model.workout

import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.SetStructure
import java.util.UUID
import kotlin.math.roundToInt

class ExerciseFactory {
    fun createExercise(movement: Movement): Exercise {
        return Exercise(
            guid = UUID.randomUUID(),
            movement = movement,
            sets = createSets(movement)
        )
    }

    private fun createSets(movement: Movement): List<Int> {
        val numSets = (movement.minSets..movement.maxSets).random()
        return when (movement.setStructures.random()) {
            SetStructure.PYRAMID -> createPyramidSets(numSets, movement.minReps, movement.maxReps)
            SetStructure.RANDOM -> createRandomSets(numSets, movement.minReps, movement.maxReps)
            SetStructure.DESCENDING -> createDescendingSets(numSets, movement.minReps, movement.maxReps)
            SetStructure.FLAT -> createFlatSets(numSets, movement.minReps, movement.maxReps)
        }
    }

    private fun createFlatSets(numSets: Int, minReps: Int, maxReps: Int): List<Int> {
        val reps = (minReps..maxReps).random()
        return List(numSets) { reps }
    }

    private fun createRandomSets(numSets: Int, minReps: Int, maxReps: Int): List<Int> {
        return List(numSets) { (minReps..maxReps).random() }
    }

    private fun createDescendingSets(numSets: Int, minReps: Int, maxReps: Int): List<Int> {
        val step: Double = (maxReps - minReps).toDouble() / numSets
        return List(numSets) {
            val thisMax: Int = maxReps - (step * it).toInt()
            val thisMin: Int = if (thisMax == minReps) minReps else maxReps - (step * (it + 1)).toInt()
            (thisMin..thisMax).random()
        }
    }

    private fun createPyramidSets(numSets: Int, minReps: Int, maxReps: Int): List<Int> {
        val step: Double = (maxReps - minReps).toDouble() / (numSets.toDouble() / 2).roundToInt()
        return List(numSets) {
            val halfIndex = numSets / 2
            val isFrontHalf = it <= halfIndex
            val stepMultiplier = if (isFrontHalf) it else halfIndex - (it - halfIndex)
            val thisMax: Int = maxReps - (step * stepMultiplier).toInt()
            val thisMin: Int = if (thisMax == minReps) minReps else maxReps - (step * (stepMultiplier + 1)).toInt()
            (thisMin..thisMax).random()
        }
    }
}