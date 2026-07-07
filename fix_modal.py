import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

start = content.find("@Composable\nfun LevelUpModal")
if start != -1:
    content = content[:start]

new_modal = """
@Composable
fun LevelUpModal(level: Int, onDismiss: () -> Unit) {
    val SysDarkBlue = Color(0xFF09121E)
    val SysBorderBlue = Color(0xFF4CB5F9)
    val SysTextBlue = Color(0xFFA0E4FF)
    val SysBorderInner = Color(0x664CB5F9)
    
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Dialog(onDismissRequest = onDismiss, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99040B13)) // Deep space background slightly transparent
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(SysDarkBlue.copy(alpha = 0.95f))
                    .border(2.dp, SysBorderBlue.copy(alpha = glowAlpha))
                    .padding(2.dp)
                    .border(1.dp, SysBorderInner)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Notification Title Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SysBorderInner)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = SysTextBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "NOTIFICATION",
                                color = Color.White,
                                fontSize = 22.sp,
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

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "You have leveled up!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Level Reached: $level",
                        color = SysTextBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = SysTextBlue,
                                blurRadius = 12f
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, SysBorderInner)
                                .clickable { onDismiss() }
                                .padding(horizontal = 32.dp, vertical = 12.dp)
                        ) {
                            Text("Confirm", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
"""

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content + new_modal)

print("Done")
