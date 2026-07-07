package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

// Colors matching the photo
val SystemBgColor = Color(0xD9060E17) // Dark, slightly transparent
val SystemBorderOuter = Color(0xFF1E88E5) // Vibrant Blue
val SystemBorderInner = Color(0xFF90CAF9) // Light Blue
val SystemGlow = Color(0xFF64B5F6)

@Composable
fun SoloSystemWindow(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .background(SystemBgColor)
            .border(2.dp, SystemBorderOuter)
            .padding(4.dp)
            .border(1.dp, SystemBorderInner.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Column {
            if (title != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.uppercase(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = SystemGlow,
                                blurRadius = 8f
                            )
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            content()
        }
        
        // Add decorative corners (top-left, top-right, bottom-left, bottom-right)
        // You could draw custom Canvas for exact corners, but simple nested borders achieve the look.
    }
}
