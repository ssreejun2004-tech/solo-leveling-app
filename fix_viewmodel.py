import sys
import re

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

# Update progressQuest
content = content.replace(
    'var leveledUp = false\n                while (newXp >= xpNeeded) {\n                    newLevel++\n                    newXp -= xpNeeded\n                    leveledUp = true\n                    xpNeeded = newLevel * 100\n                }',
    '''var leveledUp = false
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
                }'''
)

content = content.replace(
    'gold = current.gold + quest.rewardGold',
    'gold = current.gold + quest.rewardGold,\n                        availablePoints = newPoints'
)

# Update gainXp
content = content.replace(
    'var leveledUp = false\n        \n        while (newXp >= xpNeeded) {\n            newLevel++\n            newXp -= xpNeeded\n            leveledUp = true\n            xpNeeded = newLevel * 100\n        }',
    '''var leveledUp = false
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
        }'''
)

content = content.replace(
    'repository.updateStats(current.copy(level = newLevel, xp = newXp))',
    'repository.updateStats(current.copy(level = newLevel, xp = newXp, availablePoints = newPoints))'
)

# Add addStatPoint method
add_stat = """
    fun addStatPoint(statName: String) {
        viewModelScope.launch {
            val current = repository.userStats.first() ?: return@launch
            if (current.availablePoints > 0) {
                var newStr = current.str
                var newAgi = current.agi
                var newVit = current.vit
                var newSense = current.sense
                var newIntel = current.intel
                when (statName.lowercase()) {
                    "str" -> newStr += 1
                    "agi" -> newAgi += 1
                    "vit" -> newVit += 1
                    "per" -> newSense += 1
                    "int" -> newIntel += 1
                    else -> return@launch
                }
                repository.updateStats(current.copy(
                    str = newStr,
                    agi = newAgi,
                    vit = newVit,
                    sense = newSense,
                    intel = newIntel,
                    availablePoints = current.availablePoints - 1
                ))
            }
        }
    }
"""

# Insert before the last brace
content = content.rsplit('}', 1)[0] + add_stat + '}'

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)

