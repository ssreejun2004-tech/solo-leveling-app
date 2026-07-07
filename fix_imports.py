import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports = """
import androidx.compose.animation.core.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Info
"""

content = content.replace("import androidx.compose.foundation.layout.*", "import androidx.compose.foundation.layout.*\n" + imports)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
