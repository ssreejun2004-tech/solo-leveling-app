package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.QuestEntity
import com.example.data.SkillEntity
import com.example.data.SoloRepository
import com.example.data.UserStatEntity
import com.example.managers.LocationTrackerManager
import com.example.managers.FitnessTrackerManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class LeaderboardEntry(
    val rank: Int = 0,
    val name: String,
    val xp: Int,
    val isLocalUser: Boolean
)

class SoloViewModel(
    private val repository: SoloRepository,
    private val fitnessTrackerManager: FitnessTrackerManager,
    private val locationTrackerManager: LocationTrackerManager,
    private val healthConnectManager: com.example.managers.HealthConnectManager
) : ViewModel() {

    val userStats: StateFlow<UserStatEntity?> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
        
    val allQuests: StateFlow<List<QuestEntity>> = repository.allQuests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val allSkills: StateFlow<List<SkillEntity>> = repository.allSkills
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val steps: StateFlow<Int> = fitnessTrackerManager.steps
    val distance: StateFlow<Float> = locationTrackerManager.distanceMeters
    
    val healthConnectSteps: StateFlow<Long> = healthConnectManager.steps
    val heartRate: StateFlow<Long> = healthConnectManager.heartRate
    
    val leaderboard: StateFlow<List<LeaderboardEntry>> = userStats.map { stats ->
        val localXp = stats?.xp ?: 0
        val others = listOf(
            LeaderboardEntry(name = "Thomas Andre", xp = 50000, isLocalUser = false),
            LeaderboardEntry(name = "Liu Zhigang", xp = 48000, isLocalUser = false),
            LeaderboardEntry(name = "Christopher Reed", xp = 45000, isLocalUser = false),
            LeaderboardEntry(name = "Goto Ryuji", xp = 30000, isLocalUser = false),
            LeaderboardEntry(name = "Cha Hae-In", xp = 25000, isLocalUser = false),
            LeaderboardEntry(name = "Baek Yoonho", xp = 15000, isLocalUser = false),
            LeaderboardEntry(name = "Choi Jong-In", xp = 12000, isLocalUser = false)
        )
        val localUser = LeaderboardEntry(name = "Sung Jin-Woo (You)", xp = localXp, isLocalUser = true)
        
        (others + localUser).sortedByDescending { it.xp }.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _showLevelUp = kotlinx.coroutines.flow.MutableStateFlow(false)
    val showLevelUp: StateFlow<Boolean> = _showLevelUp.asStateFlow()

    private val _statMilestone = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val statMilestone: kotlinx.coroutines.flow.StateFlow<String?> = _statMilestone.asStateFlow()
    
    fun dismissStatMilestone() {
        _statMilestone.value = null
    }

    
    init {
        viewModelScope.launch {
            repository.userStats.collect { stats ->
                if (stats == null) {
                    repository.initStats(UserStatEntity())
                    
                    repository.insertQuest(QuestEntity(title = "Daily Training: Push-ups", description = "Do 100 Push-ups", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10))
                    repository.insertQuest(QuestEntity(title = "Daily Training: Sit-ups", description = "Do 100 Sit-ups", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10))
                    repository.insertQuest(QuestEntity(title = "Daily Training: Squats", description = "Do 100 Squats", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10))
                    repository.insertQuest(QuestEntity(title = "Daily Training: Plank", description = "Hold Plank for 60 Seconds", type = "DAILY", goalAmount = 60, rewardXp = 50, rewardGold = 10))
                    repository.insertQuest(QuestEntity(title = "Daily Training: Run", description = "Run 5.0 km", type = "GPS", goalAmount = 5000, rewardXp = 100, rewardGold = 20))
                    
                    repository.insertSkill(SkillEntity(name = "Sprint", description = "Increases agility momentarily", levelRequirement = 2, manaCost = 10))
                    repository.insertSkill(SkillEntity(name = "Vital Strike", description = "Critical damage attack", levelRequirement = 5, manaCost = 25))
                    repository.insertSkill(SkillEntity(name = "Stealth", description = "Become unnoticed", levelRequirement = 10, manaCost = 50))
                } else {
                    checkDailyReset()
                }
            }
        }
    }
    
    fun startTracking() {
        fitnessTrackerManager.startTracking()
        locationTrackerManager.startTracking()
        viewModelScope.launch {
            healthConnectManager.syncData()
            // Try to progress random walk quests using HealthConnect steps if any
            val hcSteps = healthConnectManager.steps.value.toInt()
            if (hcSteps > 0) {
                val quests = repository.allQuests.first()
                val walkQuest = quests.find { it.title.contains("Walk") }
                if (walkQuest != null && !walkQuest.isCompleted) {
                    val amountToUpdate = kotlin.math.min(hcSteps, walkQuest.goalAmount)
                    if (amountToUpdate > walkQuest.currentAmount) {
                         if (amountToUpdate >= walkQuest.goalAmount) {
                             progressQuest(walkQuest, amountToUpdate - walkQuest.currentAmount)
                         } else {
                             repository.updateQuest(walkQuest.copy(currentAmount = amountToUpdate))
                         }
                    }
                }
            }
        }
    }
    
    fun stopTracking() {
        fitnessTrackerManager.stopTracking()
        locationTrackerManager.stopTracking()
    }
    
    fun saveSettings(pushUps: Int, squats: Int, plank: Int, sitUps: Int, distance: Int, condition: String) {
        viewModelScope.launch {
            val stats = userStats.value ?: return@launch
            repository.updateStats(
                stats.copy(
                    dailyPushUpGoal = pushUps,
                    dailySquatGoal = squats,
                    dailyPlankGoalSeconds = plank,
                    dailySitUpGoal = sitUps,
                    dailyDistanceGoalMeters = distance,
                    healthCondition = condition
                )
            )
            
            // Also update quests goals
            allQuests.value.forEach { quest ->
                when {
                    quest.title.contains("Push-ups") -> repository.updateQuest(quest.copy(goalAmount = pushUps, description = "Do $pushUps Push-ups"))
                    quest.title.contains("Squats") -> repository.updateQuest(quest.copy(goalAmount = squats, description = "Do $squats Squats"))
                    quest.title.contains("Plank") -> repository.updateQuest(quest.copy(goalAmount = plank, description = "Hold Plank for $plank Seconds"))
                    quest.title.contains("Sit-ups") -> repository.updateQuest(quest.copy(goalAmount = sitUps, description = "Do $sitUps Sit-ups"))
                    quest.title.contains("Run") -> repository.updateQuest(quest.copy(goalAmount = distance, description = "Run ${distance / 1000.0} km"))
                }
            }
        }
    }
    fun checkDailyReset() {
        viewModelScope.launch {
            val stats = userStats.value ?: return@launch
            val currentTime = System.currentTimeMillis()
            val twentyFourHours = 24 * 60 * 60 * 1000L
            
            if (stats.lastResetTime == 0L) {
                repository.updateStats(stats.copy(lastResetTime = currentTime))
                generateRandomDailyMission()
                return@launch
            }
            
            if (currentTime - stats.lastResetTime >= twentyFourHours) {
                val quests = repository.allQuests.first()
                var anyIncomplete = false
                
                quests.forEach { quest ->
                    if (quest.type == "DAILY" || quest.type == "GPS" || quest.type == "RANDOM") {
                        if (!quest.isCompleted && quest.type != "RANDOM") { // Only strict daily/gps count for penalty
                            anyIncomplete = true
                        }
                        if (quest.type == "RANDOM") {
                             // Will be re-generated
                        } else {
                            repository.updateQuest(quest.copy(currentAmount = 0, isCompleted = false))
                        }
                    }
                }
                
                generateRandomDailyMission()
                
                val penalty = anyIncomplete || stats.penaltyActive
                
                val newStreak = if (!stats.dailyCompletedToday) 0 else stats.streakCount
                
                repository.updateStats(
                    stats.copy(
                        lastResetTime = currentTime,
                        penaltyActive = penalty,
                        streakCount = newStreak,
                        dailyCompletedToday = false
                    )
                )
                
                if (penalty) {
                     val penaltyQuest = quests.find { it.title == "PENALTY: SURVIVAL" }
                     if (penaltyQuest == null) {
                         repository.insertQuest(QuestEntity(title = "PENALTY: SURVIVAL", description = "Run from Centipedes (Survive 4 hours)", type = "PENALTY", goalAmount = 4, rewardXp = 10, rewardGold = 0))
                     } else {
                         repository.updateQuest(penaltyQuest.copy(currentAmount = 0, isCompleted = false))
                     }
                }
            }
        }
    }
    
    
    fun generateWeeklyBossRaid() {
        viewModelScope.launch {
            val quests = repository.allQuests.first()
            val existingBoss = quests.find { it.type == "BOSS" && !it.isCompleted }
            if (existingBoss == null) {
                val bossTasks = listOf(
                    QuestEntity(title = "Boss Raid: Igris the Bloodred", description = "Defeat Igris (Log 10,000 steps)", type = "BOSS", goalAmount = 10000, rewardXp = 500, rewardGold = 100),
                    QuestEntity(title = "Boss Raid: Cerberus", description = "Defeat the Gatekeeper Cerberus (Complete 500 Push-ups)", type = "BOSS", goalAmount = 500, rewardXp = 400, rewardGold = 80),
                    QuestEntity(title = "Boss Raid: Baran", description = "Defeat the Demon King Baran (Run 10km)", type = "BOSS", goalAmount = 10000, rewardXp = 800, rewardGold = 200)
                )
                repository.insertQuest(bossTasks.random())
            }
        }
    }
fun generateNewRandomQuest() {
        viewModelScope.launch {
            generateRandomDailyMission()
        }
    }
    
    private suspend fun generateRandomDailyMission() {
        val randomTasks = listOf(
            QuestEntity(title = "Daily Mission: Walk", description = "Walk 5000 steps", type = "RANDOM", goalAmount = 5000, rewardXp = 20, rewardGold = 5),
            QuestEntity(title = "Daily Mission: Squats", description = "Do 20 squats", type = "RANDOM", goalAmount = 20, rewardXp = 15, rewardGold = 5),
            QuestEntity(title = "Daily Mission: Push-ups", description = "Do 20 push-ups", type = "RANDOM", goalAmount = 20, rewardXp = 15, rewardGold = 5),
            QuestEntity(title = "Daily Mission: Sit-ups", description = "Do 30 sit-ups", type = "RANDOM", goalAmount = 30, rewardXp = 15, rewardGold = 5)
        )
        val selected = randomTasks.random()
        
        val quests = repository.allQuests.first()
        quests.filter { it.type == "RANDOM" }.forEach {
             // Delete old random quest if any, wait, there's no delete in repository.
             // We can just update it or hide it. Or we can just insert a new one if it's the same type?
             // Since we don't have delete, let's just update the existing one or insert if not found.
        }
        val existingRandom = quests.find { it.type == "RANDOM" }
        if (existingRandom != null) {
            repository.updateQuest(existingRandom.copy(
                title = selected.title,
                description = selected.description,
                goalAmount = selected.goalAmount,
                currentAmount = 0,
                isCompleted = false,
                rewardXp = selected.rewardXp,
                rewardGold = selected.rewardGold
            ))
        } else {
            repository.insertQuest(selected)
        }
    }
    
    private suspend fun checkAllDailiesCompleted() {
        val quests = repository.allQuests.first()
        val targetQuests = quests.filter { it.type == "DAILY" || it.type == "GPS" || it.type == "RANDOM" }
        if (targetQuests.isNotEmpty() && targetQuests.all { it.isCompleted }) {
            val current = repository.userStats.first() ?: return
            if (!current.dailyCompletedToday) {
                val newStreak = current.streakCount + 1
                var newXp = current.xp + (newStreak * 20)
                var newLevel = current.level
                var leveledUp = false
                var xpNeeded = newLevel * 100
                while (newXp >= xpNeeded) {
                    newLevel++
                    newXp -= xpNeeded
                    leveledUp = true
                    xpNeeded = newLevel * 100
                }
                repository.updateStats(current.copy(
                    streakCount = newStreak,
                    dailyCompletedToday = true,
                    xp = newXp,
                    level = newLevel
                ))
                if (leveledUp) {
                    _showLevelUp.value = true
                }
            }
        }
    }
    
    fun gainXp(amount: Int) {
        val current = userStats.value ?: return
        var newXp = current.xp + amount
        var newLevel = current.level
        var xpNeeded = newLevel * 100
        var leveledUp = false
        val oldLevel = current.level
        while (newXp >= xpNeeded) {
            newLevel++
            newXp -= xpNeeded
            leveledUp = true
            xpNeeded = newLevel * 100
        }
        var newPoints = current.availablePoints
        if (leveledUp) {
            newPoints += (newLevel - oldLevel) * 5
        }
        
        viewModelScope.launch {
            repository.updateStats(current.copy(level = newLevel, xp = newXp, availablePoints = newPoints))
            if (leveledUp) {
                _showLevelUp.value = true
            }
        }
    }
    
    fun dismissLevelUp() {
        _showLevelUp.value = false
    }
    
    fun progressQuest(quest: QuestEntity, defaultAmount: Int = 10) {
        if (quest.isCompleted) return
        val amount = when {
            quest.type == "GPS" -> 100
            quest.title.contains("Walk") -> 500
            quest.type == "PENALTY" -> 1
            else -> defaultAmount
        }
        if (quest.type == "GPS") { locationTrackerManager.addDistance(100f) }
        val newAmount = quest.currentAmount + amount
        if (newAmount >= quest.goalAmount) {
            viewModelScope.launch {
                repository.updateQuest(quest.copy(currentAmount = quest.goalAmount, isCompleted = true))
                
                val current = userStats.value ?: return@launch
                var newXp = current.xp + quest.rewardXp
                var newLevel = current.level
                var xpNeeded = newLevel * 100
                var leveledUp = false
                val oldLevel = current.level
                while (newXp >= xpNeeded) {
                    newLevel++
                    newXp -= xpNeeded
                    leveledUp = true
                    xpNeeded = newLevel * 100
                }
                var newPoints = current.availablePoints
                if (leveledUp) {
                    newPoints += (newLevel - oldLevel) * 5
                }
                if (quest.type == "DAILY" || quest.type == "GPS" || quest.type == "RANDOM") {
                    newPoints += 1
                }
                
                var newStr = current.str
                var newAgi = current.agi
                var newVit = current.vit
                
                if (quest.title.contains("Push-up", ignoreCase = true)) {
                    newStr += 1
                } else if (quest.title.contains("Walk", ignoreCase = true) || quest.title.contains("Run", ignoreCase = true) || quest.title.contains("steps", ignoreCase = true) || quest.type == "GPS") {
                    newAgi += 1
                } else if (quest.title.contains("Squat", ignoreCase = true) || quest.title.contains("Sit-up", ignoreCase = true) || quest.title.contains("Plank", ignoreCase = true)) {
                    newVit += 1
                } else if (quest.type == "BOSS") {
                    newStr += 2
                    newAgi += 2
                    newVit += 2
                }

                repository.updateStats(current.copy(
                    level = newLevel,
                    xp = newXp,
                    gold = current.gold + quest.rewardGold,
                        availablePoints = newPoints,
                    str = newStr,
                    agi = newAgi,
                    vit = newVit
                ))
                
                if (leveledUp) {
                    _showLevelUp.value = true
                }
                
                checkAllDailiesCompleted()
            }
        } else {
            viewModelScope.launch {
                repository.updateQuest(quest.copy(currentAmount = newAmount))
            }
        }
    }

    
    fun generateDailyQuests() {
        viewModelScope.launch {
            val quests = repository.allQuests.first()
            val dailies = quests.filter { it.type == "DAILY" }
            
            // Re-generate or initialize daily fitness tasks
            val tasks = listOf(
                QuestEntity(title = "Daily Training: Push-ups", description = "Do 100 Push-ups", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10),
                QuestEntity(title = "Daily Training: Sit-ups", description = "Do 100 Sit-ups", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10),
                QuestEntity(title = "Daily Training: Squats", description = "Do 100 Squats", type = "DAILY", goalAmount = 100, rewardXp = 50, rewardGold = 10),
                QuestEntity(title = "Daily Training: Running", description = "Run 10km", type = "DAILY", goalAmount = 10000, rewardXp = 100, rewardGold = 50),
                QuestEntity(title = "Daily Training: Plank", description = "Hold a plank for 60 seconds", type = "DAILY", goalAmount = 60, rewardXp = 30, rewardGold = 10)
            )

            val selectedTasks = tasks.shuffled().take(3)
            
            if (dailies.isEmpty()) {
                selectedTasks.forEach { repository.insertQuest(it) }
            } else {
                // Update existing dailies
                dailies.forEachIndexed { index, quest ->
                    if (index < selectedTasks.size) {
                        val task = selectedTasks[index]
                        repository.updateQuest(quest.copy(
                            title = task.title,
                            description = task.description,
                            goalAmount = task.goalAmount,
                            currentAmount = 0,
                            isCompleted = false,
                            rewardXp = task.rewardXp,
                            rewardGold = task.rewardGold
                        ))
                    }
                }
                
                // If there were fewer dailies than 3, add the rest
                if (dailies.size < selectedTasks.size) {
                    for (i in dailies.size until selectedTasks.size) {
                        repository.insertQuest(selectedTasks[i])
                    }
                }
            }
        }
    }

    fun addStatPoint(statName: String) {
        viewModelScope.launch {
            val current = userStats.value ?: return@launch
            if (current.availablePoints > 0) {
                var newStr = current.str
                var newAgi = current.agi
                var newVit = current.vit
                var newSense = current.sense
                var newIntel = current.intel
                var newMaxHp = current.maxHp
                var newHp = current.hp
                var newMaxMp = current.maxMp
                var newMp = current.mp
                
                when (statName.lowercase()) {
                    "str" -> newStr += 1
                    "agi" -> newAgi += 1
                    "vit" -> {
                        newVit += 1
                        newMaxHp += 10
                        newHp += 10
                    }
                    "per" -> newSense += 1
                    "int" -> {
                        newIntel += 1
                        newMaxMp += 10
                        newMp += 10
                    }
                    else -> return@launch
                }
                repository.updateStats(current.copy(
                    str = newStr,
                    agi = newAgi,
                    vit = newVit,
                    sense = newSense,
                    intel = newIntel,
                    maxHp = newMaxHp,
                    hp = newHp,
                    maxMp = newMaxMp,
                    mp = newMp,
                    availablePoints = current.availablePoints - 1
                ))
                
                val milestones = listOf(10, 25, 50, 75, 100)
                val updatedStatValue = when (statName.lowercase()) {
                    "str" -> newStr
                    "agi" -> newAgi
                    "vit" -> newVit
                    "per" -> newSense
                    "int" -> newIntel
                    else -> -1
                }
                if (updatedStatValue in milestones) {
                    _statMilestone.value = "${statName.uppercase()} reached $updatedStatValue!"
                }

            }
        }
    }
}

class SoloViewModelFactory(
    private val repository: SoloRepository,
    private val fitnessTrackerManager: FitnessTrackerManager,
    private val locationTrackerManager: LocationTrackerManager,
    private val healthConnectManager: com.example.managers.HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoloViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoloViewModel(repository, fitnessTrackerManager, locationTrackerManager, healthConnectManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}