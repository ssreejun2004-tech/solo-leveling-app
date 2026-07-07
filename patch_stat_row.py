import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

new_stat_row = """@Composable
fun StatRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int, canAdd: Boolean = false, onAdd: () -> Unit = {}, modifier: Modifier = Modifier) {
    var showTooltip by remember { mutableStateOf(false) }
    val description = when(label) {
        "STR" -> "Strength: Increases physical power and attack damage."
        "VIT" -> "Vitality: Increases maximum HP and stamina."
        "AGI" -> "Agility: Increases speed, evasion, and reflexes."
        "INT" -> "Intelligence: Increases maximum MP and magic power."
        "PER" -> "Perception: Increases awareness and critical hit chance."
        else -> ""
    }

    if (showTooltip) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showTooltip = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1A2A), RoundedCornerShape(8.dp))
                    .border(1.dp, SysBorderBlue, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(label, color = SysTextBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(description, color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.androidx.compose.foundation.clickable { showTooltip = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("$label:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("$value", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(4.dp))
        androidx.compose.material3.IconButton(
            onClick = onAdd,
            enabled = canAdd,
            modifier = Modifier.size(24.dp).background(
                if (canAdd) SysBorderBlue.copy(alpha = 0.3f) else SysBorderInner.copy(alpha = 0.05f), 
                androidx.compose.foundation.shape.CircleShape
            )
        ) {
            Icon(
                Icons.Filled.Add, 
                contentDescription = "Add $label", 
                tint = if (canAdd) Color.White else SysBorderInner.copy(alpha = 0.3f), 
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
"""

# Replace the existing StatRowItem
parts = content.split("@Composable\nfun StatRowItem")
if len(parts) == 2:
    # Only replace if we successfully split
    content = parts[0] + new_stat_row

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)
