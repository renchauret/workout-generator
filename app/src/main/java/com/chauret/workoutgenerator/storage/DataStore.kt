package com.chauret.workoutgenerator.storage

import com.chauret.workoutgenerator.model.Entity
import java.util.UUID

interface DataStore<T: Entity> {
    fun load(): Set<T>
    fun load(guid: UUID): T?
    fun save(entity: T)
    fun delete(guid: UUID)
}