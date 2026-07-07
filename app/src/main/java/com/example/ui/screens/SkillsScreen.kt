package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SkillEntity
import com.example.ui.theme.*

@Composable
fun SkillsScreen(skills: List<SkillEntity>, currentLevel: Int) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = "SKILL TREE",
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
        
        items(skills) { skill ->
            SkillCard(skill = skill, currentLevel = currentLevel)
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SkillCard(skill: SkillEntity, currentLevel: Int) {
    val isUnlocked = currentLevel >= skill.levelRequirement
    val borderColor = if (isUnlocked) SysBorderBlue else SysBorderInner.copy(alpha = 0.3f)
    val bgColor = if (isUnlocked) SysDarkBlue.copy(alpha = 0.9f) else SysDarkBlue.copy(alpha = 0.5f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .border(1.dp, borderColor)
            .padding(2.dp)
            .border(1.dp, if (isUnlocked) SysBorderInner else Color.Transparent)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isUnlocked) SysBorderInner.copy(alpha = 0.2f) else Color.Transparent)
                    .border(1.dp, if (isUnlocked) SysTextBlue else SysBorderInner.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Box(modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, SysTextBlue)
                        .background(Color.Transparent)
                    )
                } else {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = SysBorderInner, modifier = Modifier.size(24.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isUnlocked) "UNLOCKED SKILL" else "LVL REQ: ${skill.levelRequirement}", 
                    color = if (isUnlocked) SysTextBlue else SysBorderInner, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(skill.name, color = if (isUnlocked) Color.White else SysBorderInner, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(skill.description, color = if (isUnlocked) SysTextBlue.copy(alpha = 0.8f) else SysBorderInner.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}
