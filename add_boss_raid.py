import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

boss_code = """
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
"""

if "fun generateWeeklyBossRaid()" not in content:
    idx = content.find("fun generateNewRandomQuest()")
    content = content[:idx] + boss_code + content[idx:]
    with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
        f.write(content)
        print("Boss code added to ViewModel")
