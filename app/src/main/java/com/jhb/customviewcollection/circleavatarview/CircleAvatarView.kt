package com.jhb.customviewcollection.circleavatarview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.jhb.R
import com.jhb.customviewcollection.util.dp
import com.jhb.customviewcollection.util.getAvatar

/**
 * @author jhb
 * @date 2020/8/6
 */
private val AVATAR_SIZE = 240.dp //图片的大小
private val RADIUS = AVATAR_SIZE / 2 //裁剪圆形的半径

class CircleAvatarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private var avatar = getAvatar(R.drawable.my_avatar, AVATAR_SIZE.toInt())//图片的bitmap
    private val circlePath = Path()//圆形的路径
    private val bounds = RectF()
    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val bitmapShader = BitmapShader(avatar, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

    init {
        val scaledBitmap = Bitmap.createScaledBitmap(avatar, avatar.width * 1.2.toInt(),
                avatar.height * 1.2.toInt(), true)
        avatar = scaledBitmap


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        /**
         * 在圆心 x:View宽度的一半 y:View高度的一半 半径为图片尺寸的一半 的位置上画圆
         */
        circlePath.addCircle(width / 2f, height / 2f, RADIUS, Path.Direction.CW)
        /**
         * 设置离屏缓冲的 bounds最好不要太大,影响性能
         */
        bounds.set(width / 2f - RADIUS, height / 2f - RADIUS,
                width / 2f + RADIUS, height / 2f + RADIUS)

    }

    private val transMatrix = Matrix()
    private val marginBetweenImage = 30.dp

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //方式一
        canvas.save()
        canvas.translate(0f, -AVATAR_SIZE - marginBetweenImage)
        makeCircleAvatar1(canvas)
        canvas.restore()

//        方式二
        makeCircleAvatar2(canvas)

        //方式三
        canvas.save()
        transMatrix.reset()
        transMatrix.postTranslate(0f, AVATAR_SIZE + marginBetweenImage)
        canvas.concat(transMatrix)
        makeCircleAvatar3(canvas)
        canvas.restore()
    }

    /**
     * 利用 设置 paint 的 shader 方法
     * 有抗锯齿效果,没有毛边,效果好
     */
    private fun makeCircleAvatar3(canvas: Canvas) {

        paint.reset()
        //给paint设置Shader
        paint.shader = bitmapShader
        //以Shader的形式来画圆形
        canvas.drawPath(circlePath, paint)

    }

    /**
     * 利用 paint的 xfermode 方法
     * 有抗锯齿效果,没有毛边,效果好,做了抗锯齿的处理,填充和周边类似的半透明色等
     */
    private fun makeCircleAvatar2(canvas: Canvas) {
        //开启离屏缓冲
        val count = canvas.saveLayer(bounds, null)
        paint.reset()
        canvas.drawPath(circlePath, paint)
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_IN
        paint.xfermode = porterDuffXfermode
        //以当前的Paint来画Bitmap
        canvas.drawBitmap(avatar, width / 2 - RADIUS, height / 2 - RADIUS, paint)
        //清空paint 的 xfermode
        paint.xfermode = null
        //把离屏缓冲的内容,绘制到View上去
        canvas.restoreToCount(count)
    }

    /**
     * 利用 canvas的 clip方法
     * 没有抗锯齿效果,会有毛边,因为是精确的切像素点
     */
    private fun makeCircleAvatar1(canvas: Canvas) {
        //ktx的扩展方法,会自动保存恢复Canvas
        canvas.withSave {
            //重置画笔
            paint.reset()
            //利用Canvas来裁切去要画的范围,也就是那个圆形
            canvas.clipPath(circlePath)
            //在裁切后画上声明的Bitmap
            canvas.drawBitmap(avatar, width / 2 - RADIUS, height / 2 - RADIUS, paint)
        }
    }


}
