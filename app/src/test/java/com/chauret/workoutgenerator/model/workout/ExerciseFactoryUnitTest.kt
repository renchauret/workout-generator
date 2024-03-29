package com.chauret.workoutgenerator.model.workout

import org.junit.Before
import org.junit.Test

class ExerciseFactoryUnitTest {
    lateinit var exerciseFactory: ExerciseFactory

    @Before
    fun setup() {
        exerciseFactory = ExerciseFactory()
    }

    @Test
    fun createFlatSets_createsSameRepSets() {
        val numSets = 5
        val minReps = 2
        val maxReps = 8
        val method = ExerciseFactory.Companion::class.java.getDeclaredMethod("createFlatSets", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        method.isAccessible = true
        val params = arrayOf(numSets, minReps, maxReps, 1)
        val sets: List<Int> = method.invoke(ExerciseFactory.Companion, *params) as List<Int>
        assert(sets.size == numSets)
        assert(sets.distinct().count() == 1)
        assert(sets[0] in minReps..maxReps)
    }

    @Test
    fun createRandomSets_createsRandomSets() {
        val numSets = 5
        val minReps = 2
        val maxReps = 8
        val method = ExerciseFactory.Companion::class.java.getDeclaredMethod("createRandomSets", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        method.isAccessible = true
        val params = arrayOf(numSets, minReps, maxReps, 1)
        val sets: List<Int> = method.invoke(ExerciseFactory.Companion, *params) as List<Int>
        assert(sets.size == numSets)
        sets.forEach { assert(it in minReps..maxReps) }
        assert(sets[0] in minReps..maxReps)
    }

    @Test
    fun createDescendingSets_createsDescendingSets() {
        val numSets = 5
        val minReps = 2
        val maxReps = 8
        val method = ExerciseFactory.Companion::class.java.getDeclaredMethod("createDescendingSets", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        method.isAccessible = true
        val params = arrayOf(numSets, minReps, maxReps, 1)
        val sets: List<Int> = method.invoke(ExerciseFactory.Companion, *params) as List<Int>
        assert(sets.size == numSets)
        for (i in sets.indices) {
            assert(sets[i] in minReps..maxReps)
            if (i + 1 < sets.size) {
                assert(sets[i + 1] <= sets[i])
            }
        }
    }

    @Test
    fun createPyramidSets_createsPyramidSets() {
        val numSets = 5
        val minReps = 2
        val maxReps = 8
        val method = ExerciseFactory.Companion::class.java.getDeclaredMethod("createPyramidSets", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
        method.isAccessible = true
        val params = arrayOf(numSets, minReps, maxReps, 1)
        val sets: List<Int> = method.invoke(ExerciseFactory.Companion, *params) as List<Int>
        assert(sets.size == numSets)
        for (i in sets.indices) {
            assert(sets[i] in minReps..maxReps)
            if (i + 1 <= sets.size / 2) {
                assert(sets[i + 1] <= sets[i])
            } else if (i + 1 < sets.size) {
                assert(sets[i + 1] >= sets[i])
            }
        }
    }
}
