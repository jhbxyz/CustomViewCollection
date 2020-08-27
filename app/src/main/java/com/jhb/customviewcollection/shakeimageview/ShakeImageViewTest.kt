package com.jhb.customviewcollection.shakeimageview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.withSave
import com.jhb.customviewcollection.util.dp

/**
 * @author jhb
 * @date 2020/8/6
 * 带有红色边框的测试View
 */
class ShakeImageViewTest @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2.dp
        color = Color.RED
    }

    private val animator by lazy { ObjectAnimator.ofFloat(this, "rotation", 0f, -20f) }.apply {
        this.value.duration = 2000
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?) = true
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            shake()
            return true
        }
    })

    private val path = Path()

    init {
//        setLayerType(View.LAYER_TYPE_HARDWARE, null)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pivotX = width / 2f
        pivotY = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withSave {
            val count = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
            path.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 30f, 30f, Path.Direction.CW)
            canvas.clipPath(path)
            super.onDraw(canvas)
            canvas.restoreToCount(count)
        }

        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 30f, 30f, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }


    fun shake() {
        animator.start()
    }


}