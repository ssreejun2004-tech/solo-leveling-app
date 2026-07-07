import sys

with open("app/src/main/java/com/example/ui/theme/Color.kt", "w") as f:
    f.write("""package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val SysDarkBlue = Color(0xFF09121E)
val SysBorderBlue = Color(0xFF4CB5F9)
val SysTextBlue = Color(0xFFA0E4FF)
val SysBorderInner = Color(0x664CB5F9)
val SysGlow = Color(0xFF64B5F6)
val SysBackground = Color(0xFF040B13) // Deep space background

// Keeping some original colors for compatibility if needed, but redefining for the new theme
val TextPrimary = Color.White
val TextSecondary = SysTextBlue
val AppBackground = SysBackground
val Cyan400 = SysBorderBlue
val Cyan500 = SysBorderBlue
val FrostedBackground = SysDarkBlue.copy(alpha = 0.85f)
val FrostedBorder = SysBorderInner
val Emerald400 = Color(0xFF34D399) // text-emerald-400
val Emerald300 = Color(0xFF6EE7B7)
val Emerald600 = Color(0xFF059669)
val Emerald900 = Color(0xFF064E3B)
val Cyan900 = Color(0xFF164E63)
val Cyan300 = Color(0xFF67E8F9)
""")

