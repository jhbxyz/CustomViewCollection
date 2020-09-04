package com.jhb.customviewcollection.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import com.jhb.BuildConfig

/**
 * @author jhb
 * @date 2020/8/6
 */
enum class LogEnum {
    VERBOSE, DEBUG, INFO, WARN, ERROR
}


val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = this.toFloat().dp


fun String.log(logEnum: LogEnum = LogEnum.ERROR) {
    if (BuildConfig.DEBUG) {
        when (logEnum) {
            LogEnum.VERBOSE -> Log.v("jhb", this)
            LogEnum.DEBUG -> Log.d("jhb", this)
            LogEnum.INFO -> Log.i("jhb", this)
            LogEnum.WARN -> Log.w("jhb", this)
            LogEnum.ERROR -> Log.e("jhb", this)
        }
    }
}

fun String.logWithTag(tag: String, logEnum: LogEnum = LogEnum.ERROR) {
    if (BuildConfig.DEBUG) {
        when (logEnum) {
            LogEnum.VERBOSE -> Log.v(tag, this)
            LogEnum.DEBUG -> Log.d(tag, this)
            LogEnum.INFO -> Log.i(tag, this)
            LogEnum.WARN -> Log.w(tag, this)
            LogEnum.ERROR -> Log.e(tag, this)
        }
    }
}


fun Context.getWidthPixels() = resources.displayMetrics.widthPixels
fun Context.getHeightPixels() = resources.displayMetrics.heightPixels

fun View.getAvatar(@DrawableRes resId: Int, width: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(resources, resId, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(resources, resId, options)
}