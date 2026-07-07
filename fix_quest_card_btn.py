import sys

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "r") as f:
    content = f.read()

target = """                    Button(
                        onClick = onProgressQuest,
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan900.copy(alpha = 0.4f), contentColor = Cyan300),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Cyan500.copy(alpha = 0.5f))
                    )"""

replacement = """                    Button(
                        onClick = onProgressQuest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBoss) Color(0xFF7F1D1D).copy(alpha = 0.4f) else Cyan900.copy(alpha = 0.4f), 
                            contentColor = if (isBoss) Color(0xFFFCA5A5) else Cyan300
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isBoss) Color(0xFFEF4444).copy(alpha = 0.5f) else Cyan500.copy(alpha = 0.5f))
                    )"""

content = content.replace(target, replacement)
with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "w") as f:
    f.write(content)
print("Updated QuestCard buttons")
