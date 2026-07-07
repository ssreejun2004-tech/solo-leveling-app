import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

target = """                        if (stats.streakCount > 0) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = Color(0xFFF97316), // Orange
                                modifier = Modifier.size(20.dp).padding(end = 4.dp)
                            )
                            Text(
                                "${stats.streakCount}",
                                color = Color(0xFFF97316),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }"""

replacement = """                        if (stats.streakCount > 0) {
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

if target in content:
    content = content.replace(target, replacement)
    with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
        f.write(content)
    print("Replaced!")
else:
    print("Not found!")
