package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DashboardScreen() {
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
                    .padding(vertical = 16.dp)
                    .border(1.dp, SysBorderInner)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "STATISTICS",
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
        
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SysDarkBlue.copy(alpha = 0.85f))
                    .border(1.dp, SysBorderInner)
                    .padding(2.dp)
                    .border(1.dp, SysBorderInner.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                Column {
                    Text("ACTIVITY GRAPH", color = SysTextBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    LineChart(
                        dataPoints = listOf(10f, 25f, 15f, 40f, 35f, 60f, 55f), 
                        labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    )
                }
            }
        }
        
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SysDarkBlue.copy(alpha = 0.85f))
                    .border(1.dp, SysBorderInner)
                    .padding(2.dp)
                    .border(1.dp, SysBorderInner.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                Column {
                    Text("WORKOUT BREAKDOWN", color = SysTextBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    BarChart(
                        values = listOf(75f, 40f, 100f, 60f), 
                        labels = listOf("Push-ups", "Squats", "Sit-ups", "Running")
                    )
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun LineChart(dataPoints: List<Float>, labels: List<String>) {
    // A simplified visual representation matching the theme
    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
        Text("Chart Component - Visual Only", color = SysBorderInner)
        // Draw real graph if needed, skipped for brevity in styling
    }
}

@Composable
fun BarChart(values: List<Float>, labels: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth().height(150.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            val max = values.maxOrNull() ?: 1f
            val heightPercent = value / max
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight(heightPercent)
                        .background(SysBorderBlue)
                        .border(1.dp, SysTextBlue)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(labels.getOrNull(index) ?: "", color = SysTextBlue, fontSize = 8.sp)
            }
        }
    }
}
