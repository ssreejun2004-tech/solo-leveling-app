package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserStatEntity::class, QuestEntity::class, SkillEntity::class], version = 6, exportSchema = false)
abstract class SoloDatabase : RoomDatabase() {
    abstract fun soloDao(): SoloDao
}
