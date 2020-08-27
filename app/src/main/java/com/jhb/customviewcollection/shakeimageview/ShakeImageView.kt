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
import com.jhb.R
import com.jhb.customviewcollection.util.dp

/**
 * @author jhb
 * @date 2020/8/6
 */

class ShakeImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    //开启拍一拍效果
    private var openShake = true
    //图片的圆角大小
    private var imageCorner = 10.dp
    //图片动画执行的偏移距离
    private var offset = 3f
    //动画执行的时间
    private var duration = 500
    //圆角矩形的Path
    private val path = Path()

    init {
        //获取xml中定义的值
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ShakeImageView)
        openShake = ta.getBoolean(R.styleable.ShakeImageView_openShake, true)
        imageCorner = ta.getDimension(R.styleable.ShakeImageView_imageCorner, 10.dp)
        offset = ta.getFloat(R.styleable.ShakeImageView_offset, 3.5f)
        duration = ta.getInt(R.styleable.ShakeImageView_animDuration, 350)
        ta.recycle()

    }

    private val paint = Paint().apply {
        //开启抗锯齿
        isAntiAlias = true
    }

    //初始化动画
    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "rotation", offset, 0f, -offset, 0f, offset, 0f, -offset, 0f, offset, 0f)
    }.apply {
        this.value.duration = duration.toLong()
    }

    //初始化GestureDetector
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        //必须返回true 不然不能接管事件
        override fun onDown(e: MotionEvent?) = true

        //当双击的时候回调
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            shake()
            return true
        }
    })


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //轴心为宽的一半,高的底部
        pivotX = width / 2f
        pivotY = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        //开启离屏缓冲
        val count = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
        //path添加一个圆角矩形
        path.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), imageCorner, imageCorner, Path.Direction.CW)
        //Canvas裁切成一个圆角矩形
        canvas.clipPath(path)
        //调用AppCompatImageView的onDraw方法
        super.onDraw(canvas)
        //恢复离屏缓冲
        canvas.restoreToCount(count)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //开启拍一拍,则用gestureDetector来接管触摸事件 否则走系统默认的触摸流程
        return if (openShake) gestureDetector.onTouchEvent(event) else super.onTouchEvent(event)
    }

    //执行拍一拍的动画
    fun shake() {
        animator.start()
    }


}