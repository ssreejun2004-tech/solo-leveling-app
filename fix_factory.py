import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

# Remove addStatPoint from the factory
content = content.replace(
"""    fun addStatPoint(statName: String) {
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
    }""", "")

# Find the end of SoloViewModel class and insert it there.
# The class ends before the Factory class declaration.
factory_str = "class SoloViewModelFactory("

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

parts = content.split(factory_str)
parts[0] = parts[0].rstrip()
if parts[0].endswith("}"):
    parts[0] = parts[0][:-1] + add_stat + "}\n\n"

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(parts[0] + factory_str + parts[1])

