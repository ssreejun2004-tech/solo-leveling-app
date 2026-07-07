import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val heartRate by viewModel.heartRate.collectAsState()\n                StatusScreen(stats = userStats, steps = steps, onAddStat = { viewModel.addStatPoint(it) })',
    'val heartRate by viewModel.heartRate.collectAsState()\n                val statMilestone by viewModel.statMilestone.collectAsState()\n                StatusScreen(\n                    stats = userStats,\n                    steps = steps,\n                    onAddStat = { viewModel.addStatPoint(it) },\n                    statMilestone = statMilestone,\n                    onDismissMilestone = { viewModel.dismissStatMilestone() }\n                )'
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

