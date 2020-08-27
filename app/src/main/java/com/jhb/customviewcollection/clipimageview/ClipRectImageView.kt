package com.jhb.customviewcollection.clipimageview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.jhb.customviewcollection.util.dp

/**
 * @author jhb
 * @date 2020/8/21
 */
class ClipRectImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private var mRectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectF.set(0f,0f,width.toFloat(),height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipRect(mRectF)
        super.onDraw(canvas)
        canvas.restore()

    }

    fun clipRect(rectF: RectF) {
        mRectF.set(rectF)
       requestLayout()
    }

}