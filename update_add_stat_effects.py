import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

new_logic = """
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
"""

content = content.replace("""
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
                ))""", new_logic)

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)
