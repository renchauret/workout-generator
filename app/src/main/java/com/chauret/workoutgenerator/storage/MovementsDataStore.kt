package com.chauret.workoutgenerator.storage

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.chauret.workoutgenerator.model.movement.Movement
import com.chauret.workoutgenerator.model.movement.RepUnit
import com.chauret.workoutgenerator.model.movement.SetStructure
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID
import kotlin.random.Random

class MovementsDataStore {
    companion object {
        private const val MOVEMENTS_FILENAME = "workout_generator_movements"

        fun loadMovements(activity: FragmentActivity): Set<Movement> {
            try {
                val fis = activity.openFileInput(MOVEMENTS_FILENAME)
                val ois = ObjectInputStream(fis)
                val movements: Set<Movement> = ois.readObject() as Set<Movement>
                fis.close()
                ois.close()
                return movements
            } catch (e: Exception) {
                println("workout_generator_movements file not found, creating new")
            }
            val workoutTypes = WorkoutTypesDataStore.loadWorkoutTypes(activity)
            val movements = setOf(
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "Bench press",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Chest" }!!,
                        workoutTypes.find { it.name == "Shoulders" }!!,
                        workoutTypes.find { it.name == "Arms" }!!
                    ),
                    minSets = 3,
                    maxSets = 8,
                    minReps = 1,
                    maxReps = 8,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING,
                        SetStructure.PYRAMID
                    )
                ),
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "Shoulder press",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Shoulders" }!!,
                        workoutTypes.find { it.name == "Arms" }!!
                    ),
                    minSets = 3,
                    maxSets = 8,
                    minReps = 1,
                    maxReps = 8,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING,
                        SetStructure.PYRAMID
                    )
                ),
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "Squat",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Legs" }!!
                    ),
                    minSets = 3,
                    maxSets = 8,
                    minReps = 1,
                    maxReps = 8,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING,
                        SetStructure.PYRAMID
                    )
                ),
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "EZ Bar Curls",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Arms" }!!
                    ),
                    minSets = 2,
                    maxSets = 8,
                    minReps = 4,
                    maxReps = 20,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING,
                        SetStructure.PYRAMID
                    )
                ),
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "Triceps extensions",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Arms" }!!
                    ),
                    minSets = 2,
                    maxSets = 8,
                    minReps = 6,
                    maxReps = 20,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING,
                        SetStructure.PYRAMID,
                        SetStructure.RANDOM
                    )
                ),
                Movement(
                    guid = UUID.randomUUID(),
                    id = Random.nextLong(0, Long.MAX_VALUE),
                    name = "Pull ups",
                    workoutTypes = setOf(
                        workoutTypes.find { it.name == "Back" }!!,
                        workoutTypes.find { it.name == "Arms" }!!
                    ),
                    minSets = 2,
                    maxSets = 8,
                    minReps = 4,
                    maxReps = 10,
                    repUnit = RepUnit.REPS,
                    setStructures = setOf(
                        SetStructure.FLAT,
                        SetStructure.DESCENDING
                    )
                )
            )

            saveMovements(movements, activity)
            return movements
        }

        private fun saveMovements(movements: Set<Movement>, activity: FragmentActivity) {
            try {
                val fos: FileOutputStream =
                    activity.openFileOutput(MOVEMENTS_FILENAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(movements)
                oos.close()
                fos.close()
            } catch (e: java.lang.Exception) {
                println("Movements file not found $e")
            }
        }

        fun saveMovement(movement: Movement, activity: FragmentActivity) {
            val movements = loadMovements(activity)
            var found = false
            val newMovements = movements.map {
                if (movement.guid == it.guid) {
                    found = true
                    movement
                } else it
            }
            val finalMovements: Set<Movement> = (if (found) newMovements else newMovements + movement).toSet()
            saveMovements(finalMovements, activity)
        }

        fun deleteMovement(movementGuid: UUID, activity: FragmentActivity) {
            val movements = loadMovements(activity)
            val newMovements = movements.filter {
                it.guid != movementGuid
            }.toSet()
            saveMovements(newMovements, activity)
        }
    }
}