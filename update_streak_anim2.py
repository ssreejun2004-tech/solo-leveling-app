import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

target = """                                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                    animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
                                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                )"""

replacement = """                                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                    animation = androidx.compose.animation.core.tween<Float>(1000, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
                                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                )"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
        f.write(content)
    print("Replaced tween type!")
else:
    print("Not found!")
