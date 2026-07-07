import sys

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "r") as f:
    content = f.read()

radar_chart_code = """
@Composable
fun RadarChart(
    stats: UserStatEntity,
    modifier: Modifier = Modifier
) {
    val labels = listOf("STR", "VIT", "AGI", "INT", "PER")
    val values = listOf(stats.str, stats.vit, stats.agi, stats.intel, stats.sense)
    
    val maxStat = (values.maxOrNull() ?: 10).coerceAtLeast(20).toFloat()
    
    Box(modifier = modifier.fillMaxWidth().height(220.dp).padding(16.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = minOf(size.width, size.height) / 2 * 0.8f
            val numPoints = 5
            
            // Draw spider web
            val levels = 4
            for (level in 1..levels) {
                val levelRadius = radius * (level.toFloat() / levels)
                val path = Path()
                for (i in 0 until numPoints) {
                    val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                    val x = center.x + levelRadius * kotlin.math.cos(angle).toFloat()
                    val y = center.y + levelRadius * kotlin.math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = SysBorderInner.copy(alpha = 0.5f),
                    style = Stroke(width = 1f)
                )
            }
            
            // Draw spokes
            for (i in 0 until numPoints) {
                val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                val x = center.x + radius * kotlin.math.cos(angle).toFloat()
                val y = center.y + radius * kotlin.math.sin(angle).toFloat()
                drawLine(
                    color = SysBorderInner.copy(alpha = 0.5f),
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 1f
                )
            }
            
            // Draw stat polygon
            val statPath = Path()
            for (i in 0 until numPoints) {
                val value = values[i].toFloat().coerceAtMost(maxStat * 1.5f)
                val proportion = value / (maxStat * 1.2f)
                val statRadius = radius * proportion
                val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
                val x = center.x + statRadius * kotlin.math.cos(angle).toFloat()
                val y = center.y + statRadius * kotlin.math.sin(angle).toFloat()
                
                if (i == 0) statPath.moveTo(x, y) else statPath.lineTo(x, y)
            }
            statPath.close()
            
            drawPath(
                path = statPath,
                color = SysBorderBlue.copy(alpha = 0.4f)
            )
            drawPath(
                path = statPath,
                color = SysLightBlue,
                style = Stroke(width = 3f)
            )
        }
        
        val numPoints = 5
        for (i in 0 until numPoints) {
            val angle = (Math.PI * 2 * i / numPoints) - Math.PI / 2
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val offsetX = kotlin.math.cos(angle).toFloat() * 105
                val offsetY = kotlin.math.sin(angle).toFloat() * 105
                Text(
                    text = labels[i],
                    color = SysTextBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = offsetX.dp, y = offsetY.dp)
                )
            }
        }
    }
}
"""

if "fun RadarChart" not in content:
    content = content + radar_chart_code

with open("app/src/main/java/com/example/ui/screens/StatusScreen.kt", "w") as f:
    f.write(content)

