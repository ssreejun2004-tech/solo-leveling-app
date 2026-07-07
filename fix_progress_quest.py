import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    'if (leveledUp) {\n                    newPoints += (newLevel - oldLevel) * 5\n                }',
    'if (leveledUp) {\n                    newPoints += (newLevel - oldLevel) * 5\n                }\n                if (quest.type == "DAILY" || quest.type == "GPS" || quest.type == "RANDOM") {\n                    newPoints += 1\n                }'
)

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)
