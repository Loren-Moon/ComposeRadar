package com.loren.component.view.composeradar

import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import java.util.regex.Pattern
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by Loren on 2022/3/28
 * Description ->
 */
/**
 * 根据圆心，半径以及角度获取圆形中的xy坐标
 */
fun DrawScope.inCircleOffset(center: Offset, radius: Float, angle: Int): Offset {
    return Offset((center.x + radius * cos(angle * PI / 180)).toFloat(), (center.y + radius * sin(angle * PI / 180)).toFloat())
}

/**
 * 绘制换行文字
 */
fun DrawScope.wrapText(
    text: String,
    textPaint: TextPaint,
    width: Float,
    offset: Offset,
    currentAngle: Int,
    chineseWrapWidth: Float? = null // 用来处理UI需求中文每两个字符换行
) {
    val quadrant = quadrant(currentAngle)
    var textMaxWidth = width
    when (quadrant) {
        0 -> {
            textMaxWidth = width / 2
        }
        -1, 1, 2 -> {
            textMaxWidth = size.width - offset.x
        }
        -2, 3, 4 -> {
            textMaxWidth = offset.x
        }
    }
    // 需要特殊处理换行&&包含中文字符&&文本绘制一行的宽度>文本最大宽度
    if (chineseWrapWidth != null && isContainChinese(text) && textPaint.measureText(text) > textMaxWidth) {
        textMaxWidth = chineseWrapWidth
    }
    val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(text, 0, text.length, textPaint, textMaxWidth.toInt()).apply {
            this.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        }.build()
    } else {
        StaticLayout(text, textPaint, textMaxWidth.toInt(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false)
    }
    val textHeight = staticLayout.height
    val lines = staticLayout.lineCount
    val isWrap = lines > 1
    val textTrueWidth = if (isWrap) staticLayout.getLineWidth(0) else textPaint.measureText(text)
    drawContext.canvas.nativeCanvas.save()
    when (quadrant) {
        0 -> {
            drawContext.canvas.nativeCanvas.translate(offset.x - textTrueWidth / 2, offset.y - textHeight)
        }
        -1 -> {
            drawContext.canvas.nativeCanvas.translate(offset.x, offset.y - textHeight / 2)
        }
        -2 -> {
            drawContext.canvas.nativeCanvas.translate(offset.x - textTrueWidth, offset.y - textHeight / 2)
        }
        1 -> {
            drawContext.canvas.nativeCanvas.translate(
                offset.x,
                if (!isWrap) offset.y - textHeight / 2 else offset.y - (textHeight - textHeight / lines / 2)
            )
        }
        2 -> {
            drawContext.canvas.nativeCanvas.translate(offset.x, if (!isWrap) offset.y - textHeight / 2 else offset.y - textHeight / lines / 2)
        }
        3 -> {
            drawContext.canvas.nativeCanvas.translate(
                offset.x - textTrueWidth,
                if (!isWrap) offset.y - textHeight / 2 else offset.y - textHeight / lines / 2
            )
        }
        4 -> {
            drawContext.canvas.nativeCanvas.translate(
                offset.x - textTrueWidth,
                if (!isWrap) offset.y - textHeight / 2 else offset.y - (textHeight - textHeight / lines / 2)
            )
        }
    }
    staticLayout.draw(drawContext.canvas.nativeCanvas)
    drawContext.canvas.nativeCanvas.restore()
}

private fun isContainChinese(str: String): Boolean {
    val p = Pattern.compile("[\u4e00-\u9fa5]")
    val m = p.matcher(str)
    return m.find()
}

private fun quadrant(angle: Int): Int {
    return if (angle == -90 || angle == 90) {
        0 // 垂直
    } else if (angle == 0) {
        -1 // 水平右边
    } else if (angle == 180) {
        -2 // 水平左边
    } else if (angle > -90 && angle < 0) {
        1
    } else if (angle > 0 && angle < 90) {
        2
    } else if (angle > 90 && angle < 180) {
        3
    } else {
        4
    }
}