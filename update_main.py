import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                QuestsScreen(quests = quests, distanceMeters = distance, onProgressQuest = { quest ->
                    viewModel.progressQuest(quest, 10)
                }, onGenerateRandomQuest = {
                    viewModel.generateNewRandomQuest()
                })"""

replacement = """                QuestsScreen(quests = quests, distanceMeters = distance, onProgressQuest = { quest ->
                    viewModel.progressQuest(quest, 10)
                }, onGenerateRandomQuest = {
                    viewModel.generateNewRandomQuest()
                }, onGenerateBossRaid = {
                    viewModel.generateWeeklyBossRaid()
                })"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)
        print("Updated MainActivity.kt")
else:
    print("Could not find target in MainActivity.kt")
