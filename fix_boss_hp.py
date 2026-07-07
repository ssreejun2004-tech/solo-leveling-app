import sys

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "r") as f:
    content = f.read()

target = """            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Progress (${quest.currentAmount} / ${quest.goalAmount})", color = TextSecondary, fontSize = 12.sp)
                Text("${progressPercent}%", color = accentColor, fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }"""

replacement = """            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isBoss) {
                    Text("Boss HP: ${quest.goalAmount - quest.currentAmount} / ${quest.goalAmount}", color = Color(0xFFEF4444), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Progress (${quest.currentAmount} / ${quest.goalAmount})", color = TextSecondary, fontSize = 12.sp)
                }
                Text("${progressPercent}%", color = accentColor, fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "w") as f:
        f.write(content)
    print("Updated Boss HP")
else:
    print("Target not found")
