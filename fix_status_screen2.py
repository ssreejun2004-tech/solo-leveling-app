import sys
import re

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

parts = content.split("@Composable\nfun StatRowItem")
if len(parts) == 2:
    status_screen = parts[0].rstrip()
    
    dialog_code = """
    if (statMilestone != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = onDismissMilestone) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F1A2A), RoundedCornerShape(12.dp))
                    .border(2.dp, SysBorderBlue, RoundedCornerShape(12.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Star, contentDescription = "Milestone", tint = Color(0xFFFFD700), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MILESTONE REACHED", color = SysTextBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(statMilestone, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    androidx.compose.material3.Button(
                        onClick = onDismissMilestone,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = SysBorderBlue, contentColor = SysDarkBlue)
                    ) {
                        Text("CONFIRM", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
"""
    if status_screen.endswith("}"):
        status_screen = status_screen[:-1] + dialog_code
        
    content = status_screen + "\n\n@Composable\nfun StatRowItem" + parts[1]

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)
