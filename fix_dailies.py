import sys

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    'fun QuestsScreen(\n    quests: List<QuestEntity>,\n    distanceMeters: Float,\n    onProgressQuest: (QuestEntity) -> Unit,\n    onGenerateRandomQuest: () -> Unit,\n    onGenerateBossRaid: () -> Unit\n)',
    'fun QuestsScreen(\n    quests: List<QuestEntity>,\n    distanceMeters: Float,\n    onProgressQuest: (QuestEntity) -> Unit,\n    onGenerateRandomQuest: () -> Unit,\n    onGenerateBossRaid: () -> Unit,\n    onGenerateDailies: () -> Unit\n)'
)

buttons = """                Row {
                    Button(
                        onClick = onGenerateDailies,
                        colors = ButtonDefaults.buttonColors(containerColor = SysBorderInner.copy(alpha = 0.2f), contentColor = SysTextBlue),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SysBorderInner),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("DAILIES", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                    Button(
                        onClick = onGenerateRandomQuest,
                        colors = ButtonDefaults.buttonColors(containerColor = SysBorderInner.copy(alpha = 0.2f), contentColor = SysTextBlue),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SysBorderInner),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("RANDOM", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                    Button(
                        onClick = onGenerateBossRaid,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.4f), contentColor = Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                    ) {
                        Text("BOSS", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }"""

old_buttons = """                Row {
                    Button(
                        onClick = onGenerateRandomQuest,
                        colors = ButtonDefaults.buttonColors(containerColor = SysBorderInner.copy(alpha = 0.2f), contentColor = SysTextBlue),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SysBorderInner),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("RANDOM", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                    Button(
                        onClick = onGenerateBossRaid,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.4f), contentColor = Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                    ) {
                        Text("BOSS", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }"""

content = content.replace(old_buttons, buttons)

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "w") as f:
    f.write(content)
