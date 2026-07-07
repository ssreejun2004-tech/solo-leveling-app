import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
    'onGenerateBossRaid = {\n                    viewModel.generateWeeklyBossRaid()\n                })',
    'onGenerateBossRaid = {\n                    viewModel.generateWeeklyBossRaid()\n                }, onGenerateDailies = {\n                    viewModel.generateDailyQuests()\n                })'
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

