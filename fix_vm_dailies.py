import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

func_code = """
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
"""

content = content.replace("fun addStatPoint", func_code + "\n    fun addStatPoint")

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)

