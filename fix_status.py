import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    'fun StatusScreen(stats: UserStatEntity?, steps: Int) {',
    'fun StatusScreen(stats: UserStatEntity?, steps: Int, onAddStat: (String) -> Unit = {}) {'
)

content = content.replace(
    'StatRowItem(icon = Icons.Filled.FitnessCenter, label = "STR", value = stats.str, modifier = Modifier.weight(1f))',
    'StatRowItem(icon = Icons.Filled.FitnessCenter, label = "STR", value = stats.str, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("STR") }, modifier = Modifier.weight(1f))'
)
content = content.replace(
    'StatRowItem(icon = Icons.Filled.Favorite, label = "VIT", value = stats.vit, modifier = Modifier.weight(1f))',
    'StatRowItem(icon = Icons.Filled.Favorite, label = "VIT", value = stats.vit, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("VIT") }, modifier = Modifier.weight(1f))'
)
content = content.replace(
    'StatRowItem(icon = Icons.Filled.DirectionsRun, label = "AGI", value = stats.agi, modifier = Modifier.weight(1f))',
    'StatRowItem(icon = Icons.Filled.DirectionsRun, label = "AGI", value = stats.agi, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("AGI") }, modifier = Modifier.weight(1f))'
)
content = content.replace(
    'StatRowItem(icon = Icons.Filled.Psychology, label = "INT", value = stats.intel, modifier = Modifier.weight(1f))',
    'StatRowItem(icon = Icons.Filled.Psychology, label = "INT", value = stats.intel, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("INT") }, modifier = Modifier.weight(1f))'
)
content = content.replace(
    'StatRowItem(icon = Icons.Filled.Visibility, label = "PER", value = stats.sense, modifier = Modifier.weight(1f))',
    'StatRowItem(icon = Icons.Filled.Visibility, label = "PER", value = stats.sense, canAdd = stats.availablePoints > 0, onAdd = { onAddStat("PER") }, modifier = Modifier.weight(1f))'
)

content = content.replace(
    'text = "3", // Placeholder for unspent points',
    'text = "${stats.availablePoints}",'
)

content = content.replace(
    'fun StatRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int, modifier: Modifier = Modifier)',
    'fun StatRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: Int, canAdd: Boolean = false, onAdd: () -> Unit = {}, modifier: Modifier = Modifier)'
)

stat_row_replace = """        Text("$label:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text("$value", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        if (canAdd) {
            Spacer(modifier = Modifier.width(4.dp))
            androidx.compose.material3.IconButton(
                onClick = onAdd,
                modifier = Modifier.size(24.dp).background(SysBorderInner.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add $label", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }"""

content = content.replace(
    '        Text("$label:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)\n        Spacer(modifier = Modifier.width(8.dp))\n        Text("$value", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)',
    stat_row_replace
)

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)
