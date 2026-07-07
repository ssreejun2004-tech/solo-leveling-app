package com.example.data

import kotlinx.coroutines.flow.Flow

class SoloRepository(private val dao: SoloDao) {
    val userStats: Flow<UserStatEntity?> = dao.getUserStats()
    val allQuests: Flow<List<QuestEntity>> = dao.getAllQuests()
    val allSkills: Flow<List<SkillEntity>> = dao.getAllSkills()

    suspend fun initStats(stats: UserStatEntity) = dao.insertUserStats(stats)
    suspend fun updateStats(stats: UserStatEntity) = dao.updateUserStats(stats)

    suspend fun insertQuest(quest: QuestEntity) = dao.insertQuest(quest)
    suspend fun updateQuest(quest: QuestEntity) = dao.updateQuest(quest)

    suspend fun insertSkill(skill: SkillEntity) = dao.insertSkill(skill)
    suspend fun updateSkill(skill: SkillEntity) = dao.updateSkill(skill)
}
