package com.jhb.customviewcollection.clipimageview

import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.jhb.R
import com.jhb.customviewcollection.util.dp
import com.jhb.customviewcollection.util.log
import kotlinx.android.synthetic.main.activity_clip_rect.*


/**
 * @author jhb
 * @date 2020/8/21
 */
private const val IMAGE_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598436263157&di=d612556bc53b16844feffef440e3e90c&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fzhidao%2Fwh%253D680%252C800%2Fsign%3Dbb541c4377f40ad115b1cfe56f1c3de7%2F50da81cb39dbb6fde349ce5d0524ab18972b37be.jpg"

class ClipRectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_rect)


        Glide.with(this).asBitmap().load(IMAGE_URL)
            .into(object : ImageViewTarget<Bitmap>(civ) {
                override fun getSize(cb: SizeReadyCallback) {
                    super.getSize(cb)
                    cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL)
                }

                override fun setResource(resource: Bitmap?) {
                    resource?.apply {
                        val bitmapWidth = width
                        val bitmapHeight = height

                        "bitmapWidth  = $bitmapWidth  bitmapHeight = $bitmapHeight".log()
                        // l: 0, t: 71, r: 1715, b: 1080
                        //假设服务端提供的坐标是 0, 71, 1715, 1080
                        val createBitmap = Bitmap.createBitmap(this, 0, 71, 1715, 1080 - 71)

                        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                        paint.color = Color.RED
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = 2.dp
                        val canvas = Canvas(createBitmap)
                        val rect = Rect(0, 0, createBitmap.width, createBitmap.height)

                        canvas.drawRect(rect, paint)
                        civ.setImageBitmap(createBitmap)
                    }
                }

            })


    }


}
