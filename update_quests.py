import sys
import re

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "r") as f:
    content = f.read()

# Update signature
content = content.replace(
    "fun QuestsScreen(quests: List<QuestEntity>, distanceMeters: Float, onProgressQuest: (QuestEntity) -> Unit, onGenerateRandomQuest: () -> Unit)",
    "fun QuestsScreen(quests: List<QuestEntity>, distanceMeters: Float, onProgressQuest: (QuestEntity) -> Unit, onGenerateRandomQuest: () -> Unit, onGenerateBossRaid: () -> Unit = {})"
)

# Insert Boss Raid UI
target = """        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {"""

boss_ui = """        val bossQuests = quests.filter { it.type == "BOSS" }
        val regularQuests = quests.filter { it.type != "BOSS" }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "BOSS RAID",
                    color = Color(0xFFEF4444),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Button(
                    onClick = onGenerateBossRaid,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D).copy(alpha = 0.4f), contentColor = Color(0xFFFCA5A5)),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                ) {
                    Text("SUMMON BOSS", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }
        }
        
        items(bossQuests) { quest ->
            QuestCard(quest = quest, distanceMeters = distanceMeters, onProgressQuest = { onProgressQuest(quest) })
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {"""

content = content.replace(target, boss_ui)

# Update the generic items call
content = content.replace("items(quests) { quest ->", "items(regularQuests) { quest ->")


# Update QuestCard to handle boss UI
card_target = """    val cardBg = if (isCompleted) Emerald900.copy(alpha = 0.2f) else QuestBackground"""
card_replacement = """    val isBoss = quest.type == "BOSS"
    val accentColor = if (isCompleted) Emerald400 else if (isBoss) Color(0xFFEF4444) else Cyan500
    val cardBg = if (isCompleted) Emerald900.copy(alpha = 0.2f) else if (isBoss) Color(0xFF450A0A).copy(alpha = 0.3f) else QuestBackground"""
content = content.replace("    val accentColor = if (isCompleted) Emerald400 else Cyan500", "")
content = content.replace(card_target, card_replacement)

# Update Type tag
type_tag_target = """                    Text(
                        if (quest.type == "GPS") "GPS_SYNCED" else "DAILY", 
                        color = AppBackground, 
                        fontSize = 9.sp, 
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )"""
type_tag_replacement = """                    Text(
                        when (quest.type) {
                            "BOSS" -> "WEEKLY BOSS"
                            "GPS" -> "GPS_SYNCED"
                            "RANDOM" -> "RANDOM"
                            else -> "DAILY"
                        }, 
                        color = AppBackground, 
                        fontSize = 9.sp, 
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )"""
content = content.replace(type_tag_target, type_tag_replacement)


# Update Boss Button label
btn_label_target = """                    val label = when {
                        quest.title.contains("Plank") -> "DO PLANK (+10s)"
                        quest.type == "PENALTY" -> "SURVIVE (+1h)"
                        quest.title.contains("Walk") -> "WALK (+500 steps)"
                        else -> "DO REPS (+10)"
                    }"""
btn_label_replacement = """                    val label = when {
                        quest.type == "BOSS" -> "ATTACK BOSS (+10)"
                        quest.title.contains("Plank") -> "DO PLANK (+10s)"
                        quest.type == "PENALTY" -> "SURVIVE (+1h)"
                        quest.title.contains("Walk") -> "WALK (+500 steps)"
                        else -> "DO REPS (+10)"
                    }"""
content = content.replace(btn_label_target, btn_label_replacement)

with open("app/src/main/java/com/example/ui/screens/QuestsScreen.kt", "w") as f:
    f.write(content)
print("Updated QuestsScreen.kt")
