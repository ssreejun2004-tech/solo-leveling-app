import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

# I want the button to always be visible, but disabled when canAdd is false
old_button = """        if (canAdd) {
            Spacer(modifier = Modifier.width(4.dp))
            androidx.compose.material3.IconButton(
                onClick = onAdd,
                modifier = Modifier.size(24.dp).background(SysBorderInner.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add $label", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }"""

new_button = """        Spacer(modifier = Modifier.width(4.dp))
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
        }"""

content = content.replace(old_button, new_button)

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)
