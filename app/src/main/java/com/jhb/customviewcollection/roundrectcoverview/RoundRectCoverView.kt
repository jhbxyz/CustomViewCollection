package com.jhb.customviewcollection.roundrectcoverview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave
import com.jhb.R
import com.jhb.customviewcollection.util.dp

/**
 * @author jhb
 * @date 2020/8/10
 */
class RoundRectCoverView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mPadding = 40.dp //间距
    private var mRoundCorner = 10.dp //圆角矩形的角度
    private var mCoverColor = "#99000000".toColorInt()//遮罩的颜色

    private val bounds = RectF()
    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    private val clipPath = Path()

    init {
        //开启View级别的离屏缓冲,并关闭硬件加速，使用软件绘制
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        //通过TypeArray 获取 xml 配置的属性
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRectCoverView)
        mPadding = ta.getDimension(R.styleable.RoundRectCoverView_roundPadding, 40.dp)
        mRoundCorner = ta.getDimension(R.styleable.RoundRectCoverView_roundCorner, 10.dp)
        mCoverColor = ta.getColor(R.styleable.RoundRectCoverView_roundCoverColor, "#99000000".toColorInt())

        ta.recycle()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //设置离屏缓冲的范围
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        //设置Clip Path的矩形区域
        clipPath.addRoundRect(mPadding, mPadding, width - mPadding, height - mPadding, mRoundCorner, mRoundCorner, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        fun1(canvas)
//        fun2(canvas)

    }

    /**
     * 方法一通过 paint 的 xfermode 绘制遮罩
     */
    private fun fun1(canvas: Canvas) {
        //先画一个圆角矩形,也就是透明区域
        canvas.drawRoundRect(mPadding, mPadding, width - mPadding, height - mPadding, mRoundCorner, mRoundCorner, paint)
        //设置遮罩的颜色
        paint.color = mCoverColor
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_OUT
        paint.xfermode = porterDuffXfermode
        //画遮罩的矩形
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        //清空paint 的 xfermode
        paint.xfermode = null
    }

    /**
     * 方法二通过 canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC)绘制遮罩
     */
    private fun fun2(canvas: Canvas) {
        //Canvas的离屏缓冲
        val count = canvas.saveLayer(bounds, paint)
        //KTX的扩展函数相当于对Canvas的 save 和 restore 操作
        canvas.withSave {
            //画遮罩的颜色
            canvas.drawColor(mCoverColor)
            //按Path来裁切
            canvas.clipPath(clipPath)
            //画镂空的范围
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC)
        }
        //把离屏缓冲的内容,绘制到View上去
        canvas.restoreToCount(count)
    }
}