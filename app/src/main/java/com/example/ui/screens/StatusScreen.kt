package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserStatEntity
import com.example.ui.theme.*

// Exact colors from the image
val SysDarkBlue = Color(0xFF09121E)
val SysBorderBlue = Color(0xFF4CB5F9)
val SysTextBlue = Color(0xFFA0E4FF)
val SysLightBlue = Color(0xFF67E8F9)
val SysBorderInner = Color(0x664CB5F9)

@Composable
fun StatusScreen(stats: UserStatEntity?, steps: Int, statMilestone: String? = null, onDismissMilestone: () -> Unit = {}, onAddStat: (String) -> Unit = {}) {
    if (stats == null) return
    
    // Animate glow
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF040B13)) // Deep space background
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // The main system window
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SysDarkBlue.copy(alpha = 0.85f))
                .border(2.dp, SysBorderBlue.copy(alpha = glowAlpha))
                .padding(2.dp)
                .border(1.dp, SysBorderInner)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // STATUS Title Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .border(1.dp, SysBorderInner)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "STATUS",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 6.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = SysTextBlue,
                                blurRadius = 12f
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Level and Titles Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 32.dp)
                    ) {
                        Text(
                            text = "${stats.level}",
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = SysTextBlue,
                                    blurRadius = 16f
                                )
                            )
                        )
                        Text(
                            text = "LEVEL",
                            color = SysTextBlue,
                            fontSize = 12.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row {
                            Text("JOB: ", color = SysTextBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("None", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row {
                            Text("TITLE: ", color = SysTextBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Wolf Assassin", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // HP / MP / Fatigue Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SysBorderInner)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // HP
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Filled.Add, contentDescription = "HP", tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Box(modifier = Modifier.width(100.dp).height(6.dp).background(SysBorderInner, RoundedCornerShape(3.dp))) {
                                    Box(modifier = Modifier.fillMaxWidth(if(stats.maxHp > 0) stats.hp.toFloat()/stats.maxHp else 0f).fillMaxHeight().background(Color.White, RoundedCornerShape(3.dp)))
                                }
                                Text("${stats.hp}/${stats.maxHp}", color = SysTextBlue, fontSize = 10.sp, modifier = Modifier.align(Alignment.End))
                            }
                        }
                        
                        Text("HP", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))

                        // MP
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Filled.LocalDrink, contentDescription = "MP", tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Box(modifier = Modifier.width(100.dp).height(6.dp).background(SysBorderInner, RoundedCornerShape(3.dp))) {
                                    Box(modifier = Modifier.fillMaxWidth(if(stats.maxMp > 0) stats.mp.toFloat()/stats.maxMp else 0f).fillMaxHeight().background(Color.White, RoundedCornerShape(3.dp)))
                                }
                                Text("${stats.mp}/${stats.maxMp}", color = SysTextBlue, fontSize = 10.sp, modifier = Modifier.align(Alignment.End))
                            }
                        }
                        
                        Text("MP", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))

                        // Fatigue
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.DataSaverOff, contentDescription = "Fatigue", tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("FATIGUE: 0", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RadarChart(stats = stats)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Stats Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SysBorderInner)
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatRowItem(icon = Icons.Filled.FitnessCenter, label = "STR", value = stats.str, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("STR") }, modifier = Modifier.weight(1f))
                            StatRowItem(icon = Icons.Filled.Favorite, label = "VIT", value = stats.vit, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("VIT") }, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatRowItem(icon = Icons.Filled.DirectionsRun, label = "AGI", value = stats.agi, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("AGI") }, modifier = Modifier.weight(1f))
                            StatRowItem(icon = Icons.Filled.Psychology, label = "INT", value = stats.intel, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("INT") }, modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            StatRowItem(icon = Icons.Filled.Visibility, label = "PER", value = stats.sense, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("PER") }, modifier = Modifier.weight(1f))
                            
                            // Available points
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                    Text("Available", color = SysTextBlue, fontSize = 10.sp)
                                    Text("Ability", color = SysTextBlue, fontSize = 10.sp)
                                    Text("Points", color = SysTextBlue, fontSize = 10.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${stats.availablePoints}",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    




    if (statMilestone != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = onDismissMilestone) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1A2A), RoundedCornerShape(12.dp))
                    .border(2.dp, SysBorderBlue, RoundedCornerShape(12.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Star, contentDescription = "Milestone", tint = Color(0xFFFFD700), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MILESTONE REACHED", color = SysTextBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(statMilestone, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    androidx.compose.material3.Button(
                        onClick = onDismissMilestone,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = SysBorderBlue, contentColor = SysDarkBlue)
                    ) {
                        Text("CONFIRM", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


@Composable
fun StatRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int, canAdd: Boolean = false, onAdd: () -> Unit = {}, modifier: Modifier = Modifier) {
    var showTooltip by remember { mutableStateOf(false) }
    val description = when(label) {
        "STR" -> "Strength: Increases physical power and attack damage."
        "VIT" -> "Vitality: Increases maximum HP and stamina."
        "AGI" -> "Agility: Increases speed, evasion, and reflexes."
        "INT" -> "Intelligence: Increases maximum MP and magic power."
        "PER" -> "Perception: Increases awareness and critical hit chance."
        else -> ""
    }

    if (showTooltip) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showTooltip = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1A2A), RoundedCornerShape(8.dp))
                    .border(1.dp, SysBorderBlue, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(label, color = SysTextBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(description, color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.clickable { showTooltip = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("$label:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("$value", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(4.dp))
        androidx.compose.material3.IconButton(
            onClick = onAdd,
            enabled = canAdd,
            modifier = Modifier.size(24.dp).background(
                if (canAdd) SysBorderBlue.copy(alpha = 0.3f) else SysBorderInner.copy(alpha = 0.05f), 
                androidx.compose.foundation.shape.CircleShape
            )
        ) {
            Icon(
                Icons.Filled.Add, 
                contentDescription = "Add $label", 
                tint = if (canAdd) Color.White else SysBorderInner.copy(alpha = 0.3f), 
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun RadarChart(
    stats: UserStatEntity,
    modifier: Modifier = Modifier
) {
    val labels = listOf("STR", "VIT", "AGI", "INT", "PER")
    val values = listOf(stats.str, stats.vit, stats.agi, stats.intel, stats.sense)
    
    val maxStat = (values.maxOrNull() ?: 10).coerceAtLeast(20).toFloat()
    
    Box(modifier = modifier.fillMaxWidth().height(220.dp).padding(16.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = minOf(size.width, size.height) / 2 * 0.8f
            val numPoints = 5
            
            // Draw spider web
            val levels = 4
            for (level in 1..levels) {
                val levelRadius = radius * (level.toFloat() / levels)
                val path = Path()
                for (i in 0 until numPoints) {
                    val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                    val x = center.x + levelRadius * kotlin.math.cos(angle).toFloat()
                    val y = center.y + levelRadius * kotlin.math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = SysBorderInner.copy(alpha = 0.5f),
                    style = Stroke(width = 1f)
                )
            }
            
            // Draw spokes
            for (i in 0 until numPoints) {
                val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                val x = center.x + radius * kotlin.math.cos(angle).toFloat()
                val y = center.y + radius * kotlin.math.sin(angle).toFloat()
                drawLine(
                    color = SysBorderInner.copy(alpha = 0.5f),
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 1f
                )
            }
            
            // Draw stat polygon
            val statPath = Path()
            for (i in 0 until numPoints) {
                val value = values[i].toFloat().coerceAtMost(maxStat * 1.5f)
                val proportion = value / (maxStat * 1.2f)
                val statRadius = radius * proportion
                val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                val x = center.x + statRadius * kotlin.math.cos(angle).toFloat()
                val y = center.y + statRadius * kotlin.math.sin(angle).toFloat()
                
                if (i == 0) statPath.moveTo(x, y) else statPath.lineTo(x, y)
            }
            statPath.close()
            
            drawPath(
                path = statPath,
                color = SysBorderBlue.copy(alpha = 0.4f)
            )
            drawPath(
                path = statPath,
                color = SysLightBlue,
                style = Stroke(width = 3f)
            )
        }
        
        val numPoints = 5
        for (i in 0 until numPoints) {
            val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val offsetX = kotlin.math.cos(angle).toFloat() * 105
                val offsetY = kotlin.math.sin(angle).toFloat() * 105
                Text(
                    text = labels[i],
                    color = SysTextBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = offsetX.dp, y = offsetY.dp)
                )
            }
        }
    }
}
