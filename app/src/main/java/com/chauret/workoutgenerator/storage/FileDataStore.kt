package com.chauret.workoutgenerator.storage

import android.content.Context
import com.chauret.workoutgenerator.model.Entity
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.UUID

class FileDataStore<T : Entity>(
    private val filename: String,
    private val context: Context
): DataStore<T> {
    override fun load(): Set<T> {
        try {
            val fis = context.openFileInput(filename)
            val ois = ObjectInputStream(fis)
            val entities: Set<T> = ois.readObject() as Set<T>
            fis.close()
            ois.close()
            return entities
        } catch (e: Exception) {
            println("$filename file failed to process, creating new")
        }
        val entities = setOf<T>()
        save(entities)
        return entities
    }

    override fun load(guid: UUID): T? {
        return load().find { it.guid == guid }
    }

    override fun save(entity: T) {
        val entities = load()
        var found = false
        val newEntities = entities.map {
            if (entity.guid == it.guid) {
                found = true
                entity
            } else it
        }
        val finalEntities: Set<T> = (if (found) newEntities else newEntities + entity).toSet()
        save(finalEntities)
    }

    override fun delete(guid: UUID) {
        val entities = load()
        val newEntities = entities.filter {
            it.guid != guid
        }.toSet()
        save(newEntities)
    }

    private fun save(entities: Set<T>) {
        try {
            val fos: FileOutputStream =
                context.openFileOutput(filename, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(entities)
            oos.close()
            fos.close()
        } catch (e: java.lang.Exception) {
            println("$filename file not found $e")
        }
    }
}