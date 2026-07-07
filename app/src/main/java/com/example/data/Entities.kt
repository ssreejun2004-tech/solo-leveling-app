package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "user_stats")
data class UserStatEntity(
    @PrimaryKey val id: Int = 1, // Singleton row
    val level: Int = 1,
    val xp: Int = 0,
    val hp: Int = 100,
    val maxHp: Int = 100,
    val mp: Int = 50,
    val maxMp: Int = 50,
    val str: Int = 10,
    val agi: Int = 10,
    val vit: Int = 10,
    val sense: Int = 10,
    val intel: Int = 10,
    val gold: Int = 0,
    val dailyPushUpGoal: Int = 100,
    val dailySquatGoal: Int = 100,
    val dailyPlankGoalSeconds: Int = 60,
    val dailySitUpGoal: Int = 100,
    val dailyDistanceGoalMeters: Int = 5000,
    val healthCondition: String = "Normal",
    val lastResetTime: Long = 0L,
    val penaltyActive: Boolean = false,
    val streakCount: Int = 0,
    val availablePoints: Int = 0,
    val dailyCompletedToday: Boolean = false
)

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val type: String, // "DAILY", "GPS"
    val goalAmount: Int,
    val currentAmount: Int = 0,
    val rewardXp: Int,
    val rewardGold: Int,
    val isCompleted: Boolean = false,
    val targetLat: Double = 0.0,
    val targetLng: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "skills")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val levelRequirement: Int,
    val manaCost: Int,
    val isUnlocked: Boolean = false
)
