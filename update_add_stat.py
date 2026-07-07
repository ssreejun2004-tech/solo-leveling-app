import sys

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    'val current = repository.userStats.first() ?: return@launch',
    'val current = userStats.value ?: return@launch'
)

with open("app/src/main/java/com/example/viewmodel/SoloViewModel.kt", "w") as f:
    f.write(content)
