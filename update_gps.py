import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

new_content = content.replace(
    'val newAmount = quest.currentAmount + amount',
    'if (quest.type == "GPS") { locationTrackerManager.addDistance(100f) }\n        val newAmount = quest.currentAmount + amount'
)

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(new_content)
