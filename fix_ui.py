import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

content = content.replace("package com.example.ui.screens", "package com.example.ui.screens\nimport androidx.compose.ui.draw.drawBehind\nimport androidx.compose.ui.graphics.Path\nimport androidx.compose.ui.graphics.drawscope.Stroke")

# We want to redesign StatusScreen to look exactly like the image
# First, the root layout should have a dark, blurry background
replacement = """
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF040A12))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
"""
# Need to replace the whole content of StatusScreen
