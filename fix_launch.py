import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports_to_add = """
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
"""

# Insert imports at the top
import_idx = content.find("import ")
if import_idx != -1:
    content = content[:import_idx] + imports_to_add + content[import_idx:]
else:
    print("Could not find imports")

# Also simplify animatable usage
content = content.replace("androidx.compose.animation.core.", "")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
