package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserStatEntity
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    stats: UserStatEntity?,
    onSaveSettings: (pushUps: Int, squats: Int, plank: Int, sitUps: Int, distance: Int, condition: String) -> Unit
) {
    if (stats == null) {
        Box(modifier = Modifier.fillMaxSize().background(AppBackground), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Cyan500)
        }
        return
    }

    var pushUps by remember { mutableStateOf(stats.dailyPushUpGoal.toFloat()) }
    var squats by remember { mutableStateOf(stats.dailySquatGoal.toFloat()) }
    var plank by remember { mutableStateOf(stats.dailyPlankGoalSeconds.toFloat()) }
    var sitUps by remember { mutableStateOf(stats.dailySitUpGoal.toFloat()) }
    var distance by remember { mutableStateOf(stats.dailyDistanceGoalMeters.toFloat() / 1000f) }
    var condition by remember { mutableStateOf(stats.healthCondition) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "SYSTEM SETTINGS",
                color = Cyan500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }

        item {
            SettingCard(title = "DAILY GOALS") {
                Text("Push-ups Goal: ${pushUps.toInt()}", color = Color.White)
                Slider(
                    value = pushUps,
                    onValueChange = { pushUps = it },
                    valueRange = 10f..500f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = Cyan400, activeTrackColor = Cyan500)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Squats Goal: ${squats.toInt()}", color = Color.White)
                Slider(
                    value = squats,
                    onValueChange = { squats = it },
                    valueRange = 10f..500f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = Cyan400, activeTrackColor = Cyan500)
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Plank Goal: ${plank.toInt()} sec", color = Color.White)
                Slider(
                    value = plank,
                    onValueChange = { plank = it },
                    valueRange = 10f..300f,
                    steps = 29,
                    colors = SliderDefaults.colors(thumbColor = Cyan400, activeTrackColor = Cyan500)
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Sit-ups Goal: ${sitUps.toInt()}", color = Color.White)
                Slider(
                    value = sitUps,
                    onValueChange = { sitUps = it },
                    valueRange = 10f..500f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = Cyan400, activeTrackColor = Cyan500)
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Running Distance: ${String.format("%.1f", distance)} km", color = Color.White)
                Slider(
                    value = distance,
                    onValueChange = { distance = it },
                    valueRange = 0.5f..20f,
                    steps = 39,
                    colors = SliderDefaults.colors(thumbColor = Emerald400, activeTrackColor = Emerald600)
                )
            }
        }
        
        item {
            SettingCard(title = "HEALTH CONDITION") {
                val conditions = listOf("Normal", "Fatigued", "Injured")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    conditions.forEach { c ->
                        val isSelected = condition == c
                        Button(
                            onClick = { condition = c },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Cyan500 else FrostedBackground,
                                contentColor = if (isSelected) AppBackground else TextSecondary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(c, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        
        item {
            Button(
                onClick = {
                    onSaveSettings(pushUps.toInt(), squats.toInt(), plank.toInt(), sitUps.toInt(), (distance * 1000).toInt(), condition)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Cyan900.copy(alpha = 0.6f), contentColor = Cyan300),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan500)
            ) {
                Text("SAVE SETTINGS", fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
        }
    }
}

@Composable
fun SettingCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FrostedBackground, RoundedCornerShape(12.dp))
            .border(1.dp, FrostedBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(title, color = Cyan500, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}
