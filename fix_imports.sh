#!/bin/bash
sed -i 's/^import Animatable/import androidx.compose.animation.core.Animatable/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import tween/import androidx.compose.animation.core.tween/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import spring/import androidx.compose.animation.core.spring/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import Spring/import androidx.compose.animation.core.Spring/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import infiniteRepeatable/import androidx.compose.animation.core.infiniteRepeatable/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import LinearOutSlowInEasing/import androidx.compose.animation.core.LinearOutSlowInEasing/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import LinearEasing/import androidx.compose.animation.core.LinearEasing/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/^import RepeatMode/import androidx.compose.animation.core.RepeatMode/g' app/src/main/java/com/example/MainActivity.kt
