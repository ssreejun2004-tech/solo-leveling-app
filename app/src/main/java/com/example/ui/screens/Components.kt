package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NeonBar(
    current: Int,
    max: Int,
    labelColor: Color,
    gradientColors: List<Color>,
    borderColor: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    val progress = if (max > 0) current.toFloat() / max.toFloat() else 0f
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = labelColor, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 2.sp)
            Text("$current / $max", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF1E293B), shape = CircleShape)
                .border(1.dp, borderColor, shape = CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(Brush.horizontalGradient(gradientColors), shape = CircleShape)
            )
        }
    }
}
