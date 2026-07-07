import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    '// Stats Box',
    'RadarChart(stats = stats)\n                Spacer(modifier = Modifier.height(16.dp))\n                \n                // Stats Box'
)

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)

