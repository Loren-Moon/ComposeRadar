package com.loren.component.view.composeradar

import android.text.TextPaint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by Loren on 2022/3/28
 * Description ->
 */
@Composable
fun ComposeRadarView(
    modifier: Modifier,
    data: List<RadarBean>,
    specialHandle: Boolean = false
) {
    val CIRCLE_TURN = 3
    val colors =
        arrayOf(Color(0xffeff3fe), Color(0xffe8eefd), Color(0xffdee6fc), Color(0xffd8e0fa), Color(0xffc3d0f7), Color(0x5989A3F0), Color(0xff3a65e6))
    var enable by remember {
        mutableStateOf(false)
    }
    val progress by animateFloatAsState(if (enable) 1f else 0f, animationSpec = tween(2000))
    Canvas(modifier = modifier
        .border(1.dp, colors[6])
        .drawWithCache {
            val center = Offset(size.width / 2, size.height / 2)
            val textNeedRadius = 25.dp.toPx()
            val radarRadius = center.x - textNeedRadius
            val turnRadius = radarRadius / CIRCLE_TURN

            val itemAngle = 360 / data.size
            val startAngle = if (data.size % 2 == 0) {
                -90 - itemAngle / 2
            } else {
                -90
            }
            val textPaint = TextPaint().apply {
                isAntiAlias = true
                textSize = 10.sp.toPx()
                color = colors[6].toArgb()
            }
            val path = Path()
            onDrawWithContent {
                path.reset()
                // 绘制圆形
                for (turn in 0 until CIRCLE_TURN) {
                    drawCircle(colors[turn], radius = turnRadius * (CIRCLE_TURN - turn))
                    drawCircle(colors[3], radius = turnRadius * (CIRCLE_TURN - turn), style = Stroke(2f))
                }

                for (index in data.indices) {
                    // 绘制虚线
                    val currentAngle = startAngle + itemAngle * index
                    val xy = inCircleOffset(center, progress * radarRadius, currentAngle)
                    drawLine(colors[4], center, xy, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))

                    // 绘制文字
                    val textPointRadius = progress * radarRadius + 10f
                    val offset = inCircleOffset(center, textPointRadius, currentAngle)
                    val text = data[index].text
                    wrapText(
                        text,
                        textPaint,
                        size.width,
                        offset,
                        currentAngle,
                        if (specialHandle) textPaint.textSize * 2 else null
                    )

                    // 绘制连接范围
                    val pointData = data[index]
                    val pointRadius = progress * radarRadius * pointData.value / 100
                    val fixPoint = inCircleOffset(center, pointRadius, currentAngle)
                    if (index == 0) {
                        path.moveTo(fixPoint.x, fixPoint.y)
                    } else {
                        path.lineTo(fixPoint.x, fixPoint.y)
                    }
                }
                path.close()
                drawPath(path, colors[5])
                drawPath(path, colors[6], style = Stroke(5f))
            }
        }
        .onGloballyPositioned {
            enable = it.boundsInRoot().top >= 0 && it.boundsInRoot().right > 0
        }) {}
}

data class RadarBean(
    val text: String,
    val value: Float
)