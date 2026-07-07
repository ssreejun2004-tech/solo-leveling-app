import sys

with open("app/src/main/java/com/example/data/Entities.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val streakCount: Int = 0,',
    'val streakCount: Int = 0,\n    val availablePoints: Int = 0,'
)

with open("app/src/main/java/com/example/data/Entities.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/data/SoloDatabase.kt", "r") as f:
    content = f.read()

content = content.replace('version = 5', 'version = 6')

with open("app/src/main/java/com/example/data/SoloDatabase.kt", "w") as f:
    f.write(content)
