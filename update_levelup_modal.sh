#!/bin/bash
cat << 'INNER_EOF' > /tmp/LevelUpModal.kt
@Composable
fun LevelUpModal(level: Int, onDismiss: () -> Unit) {
    val scale = remember { androidx.compose.animation.core.Animatable(0.5f) }
    val alpha = remember { androidx.compose.animation.core.Animatable(0f) }
    val glowAlpha = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.launch {
            alpha.animateTo(1f, animationSpec = androidx.compose.animation.core.tween(400, easing = androidx.compose.animation.core.LinearOutSlowInEasing))
        }
        kotlinx.coroutines.launch {
            scale.animateTo(1f, animationSpec = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessLow))
        }
        kotlinx.coroutines.launch {
            glowAlpha.animateTo(0.8f, animationSpec = androidx.compose.animation.core.tween(500))
            glowAlpha.animateTo(0.4f, animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.LinearEasing),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            ))
        }
    }

    Dialog(onDismissRequest = onDismiss, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f * alpha.value))
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Screen-wide pulsating glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(Cyan500.copy(alpha = glowAlpha.value * 0.7f), Color.Transparent),
                                radius = size.width
                            )
                        )
                    }
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        alpha = alpha.value
                    )
                    .background(AppBackground.copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                    .border(2.dp, Cyan500.copy(alpha = alpha.value), RoundedCornerShape(16.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "LEVEL UP!",
                        color = Cyan300,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 6.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Cyan500,
                                blurRadius = 25f * glowAlpha.value
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "You are now Level $level",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Cyan500,
                                blurRadius = 10f * glowAlpha.value
                            )
                        )
                    )
                    
                    val playerRank = when {
                        level >= 50 -> "S"
                        level >= 40 -> "A"
                        level >= 30 -> "B"
                        level >= 20 -> "C"
                        level >= 10 -> "D"
                        else -> "E"
                    }
                    val rankColor = when (playerRank) {
                        "S" -> Color(0xFFEAB308)
                        "A" -> Color(0xFFEF4444)
                        "B" -> Color(0xFF8B5CF6)
                        "C" -> Emerald400
                        "D" -> Cyan400
                        else -> TextSecondary
                    }
                    
                    if (level == 10 || level == 20 || level == 30 || level == 40 || level == 50) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "RANK UP!",
                            color = rankColor,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 4.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = rankColor,
                                    blurRadius = 20f * glowAlpha.value
                                )
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan500.copy(alpha = 0.2f), contentColor = Cyan300),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Cyan500.copy(alpha = 0.5f))
                    ) {
                        Text("ACCEPT", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    }
                }
            }
        }
    }
}
INNER_EOF
