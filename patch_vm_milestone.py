import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

# Add StateFlow for milestone
milestone_state = """    private val _statMilestone = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val statMilestone: kotlinx.coroutines.flow.StateFlow<String?> = _statMilestone.asStateFlow()
    
    fun dismissStatMilestone() {
        _statMilestone.value = null
    }
"""

if "_statMilestone" not in content:
    content = content.replace(
        'val showLevelUp: StateFlow<Boolean> = _showLevelUp.asStateFlow()',
        'val showLevelUp: StateFlow<Boolean> = _showLevelUp.asStateFlow()\n\n' + milestone_state
    )

milestone_logic = """                repository.updateStats(current.copy(
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
                }"""

content = content.replace("""                repository.updateStats(current.copy(
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
                ))""", milestone_logic)

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)

