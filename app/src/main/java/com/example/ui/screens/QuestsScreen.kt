package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuestEntity
import com.example.ui.theme.*

@Composable
fun QuestsScreen(
    quests: List<QuestEntity>,
    distanceMeters: Float,
    onProgressQuest: (QuestEntity) -> Unit,
    onGenerateRandomQuest: () -> Unit,
    onGenerateBossRaid: () -> Unit,
    onGenerateDailies: () -> Unit
) {
    val bossQuests = quests.filter { it.type == "BOSS" }
    val regularQuests = quests.filter { it.type != "BOSS" }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, SysBorderInner)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "QUEST LOG",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = SysTextBlue,
                            blurRadius = 8f
                        )
                    )
                )
            }
        }
        
        if (bossQuests.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "URGENT QUESTS",
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
            }
            
            items(bossQuests) { quest ->
                QuestCard(quest = quest, distanceMeters = distanceMeters, onProgressQuest = { onProgressQuest(quest) })
            }
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "SYSTEM QUESTS",
                    color = SysTextBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                
                Row {
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
                }
            }
        }
        
        items(regularQuests) { quest ->
            QuestCard(quest = quest, distanceMeters = distanceMeters, onProgressQuest = { onProgressQuest(quest) })
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun QuestCard(quest: QuestEntity, distanceMeters: Float, onProgressQuest: () -> Unit) {
    val isCompleted = quest.isCompleted
    val isBoss = quest.type == "BOSS"
    
    val borderColor = if (isCompleted) Emerald400 else if (isBoss) Color(0xFFEF4444) else SysBorderBlue
    val innerBorder = if (isCompleted) Emerald400.copy(alpha = 0.5f) else if (isBoss) Color(0xFFEF4444).copy(alpha = 0.5f) else SysBorderInner
    val bgColor = SysDarkBlue.copy(alpha = 0.85f)
    val textColor = if (isCompleted) Emerald300 else if (isBoss) Color(0xFFFCA5A5) else SysTextBlue
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .border(1.dp, innerBorder)
            .padding(1.dp)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 12.dp.toPx() 
                )
            }
            .padding(start = 8.dp)
            .border(1.dp, innerBorder.copy(alpha = 0.3f))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (isCompleted) "COMPLETED" else "ACTIVE QUEST", 
                        color = textColor, 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 12.sp, 
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(quest.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp)
                }
                Box(
                    modifier = Modifier
                        .border(1.dp, textColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        when (quest.type) {
                            "BOSS" -> "WEEKLY BOSS"
                            "GPS" -> "GPS_SYNCED"
                            "RANDOM" -> "RANDOM"
                            else -> "DAILY"
                        }, 
                        color = textColor, 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(quest.description, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            val progressPercent = if (quest.goalAmount > 0) ((quest.currentAmount.toFloat() / quest.goalAmount) * 100).toInt() else 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isBoss) {
                    Text("Boss HP: ${quest.goalAmount - quest.currentAmount} / ${quest.goalAmount}", color = Color(0xFFEF4444), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("Progress (${quest.currentAmount} / ${quest.goalAmount})", color = SysTextBlue, fontSize = 12.sp)
                }
                Text("${progressPercent}%", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Reward: ${quest.rewardXp} EXP", color = Color(0xFFFBBF24), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Reward: ${quest.rewardGold} G", color = Color(0xFFFBBF24), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            
            if (!isCompleted) {
                Spacer(modifier = Modifier.height(16.dp))
                if (quest.type == "GPS") {
                    Text("Tracked Distance: ${String.format("%.1f", distanceMeters)}m", color = textColor, fontSize = 12.sp)
                    Button(
                        onClick = onProgressQuest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBoss) Color(0xFF450A0A) else SysDarkBlue, 
                            contentColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, innerBorder)
                    ) {
                        Text("SIMULATE RUN (+100m)", fontWeight = FontWeight.Bold)
                    }
                } else {
                    val label = when {
                        quest.type == "BOSS" -> "ATTACK BOSS (+10)"
                        quest.title.contains("Plank") -> "DO PLANK (+10s)"
                        quest.type == "PENALTY" -> "SURVIVE (+1h)"
                        quest.title.contains("Walk") -> "WALK (+500 steps)"
                        else -> "DO REPS (+10)"
                    }
                    Button(
                        onClick = onProgressQuest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBoss) Color(0xFF450A0A) else SysDarkBlue, 
                            contentColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, innerBorder)
                    ) {
                        Text(label, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    }
                }
            }
        }
    }
}
