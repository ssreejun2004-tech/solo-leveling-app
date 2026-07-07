package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.LeaderboardEntry
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen(entries: List<LeaderboardEntry>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    text = "HUNTER RANKINGS",
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
        
        items(entries) { entry ->
            val isTop3 = entry.rank <= 3
            val borderColor = if (entry.isLocalUser) SysBorderBlue else if (isTop3) Color(0xFFFBBF24) else SysBorderInner.copy(alpha = 0.3f)
            val bgColor = if (entry.isLocalUser) SysDarkBlue.copy(alpha = 0.9f) else SysDarkBlue.copy(alpha = 0.5f)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .border(1.dp, borderColor)
                    .padding(2.dp)
                    .border(1.dp, if (entry.isLocalUser) SysBorderInner else Color.Transparent)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "#${entry.rank}",
                        color = if (isTop3) Color(0xFFFBBF24) else SysTextBlue,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.width(40.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SysBorderInner.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, borderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = if (entry.isLocalUser) Color.White else SysTextBlue, modifier = Modifier.size(20.dp))
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            entry.name, 
                            color = if (entry.isLocalUser) Color.White else SysTextBlue, 
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (entry.isLocalUser) {
                            Text("YOU", color = SysTextBlue, fontSize = 8.sp, letterSpacing = 2.sp)
                        }
                    }
                    
                    Text(
                        "${entry.xp} EXP",
                        color = Color(0xFFFBBF24),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
