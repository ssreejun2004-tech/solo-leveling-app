import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

target = """                        if (stats.streakCount > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = Color(0xFFF97316), // Orange
                                    modifier = Modifier.size(20.dp).padding(end = 4.dp)
                                )
                                Text(
                                    "${stats.streakCount} DAY STREAK",
                                    color = Color(0xFFF97316),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = androidx.compose.ui.graphics.Shadow(
                                            color = Color(0xFFF97316),
                                            blurRadius = 15f
                                        )
                                    )
                                )
                            }
                        }"""

replacement = """                        if (stats.streakCount > 0) {
                            val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                            val glowRadius by infiniteTransition.animateFloat(
                                initialValue = 10f,
                                targetValue = 25f,
                                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                    animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
                                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = Color(0xFFF97316), // Orange
                                    modifier = Modifier.size(20.dp).padding(end = 4.dp)
                                )
                                Text(
                                    "${stats.streakCount} DAY STREAK",
                                    color = Color(0xFFF97316),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = androidx.compose.ui.graphics.Shadow(
                                            color = Color(0xFFF97316),
                                            blurRadius = glowRadius
                                        )
                                    )
                                )
                            }
                        }"""

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
        f.write(content)
    print("Replaced!")
else:
    print("Not found!")
