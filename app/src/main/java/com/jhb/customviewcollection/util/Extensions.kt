package com.jhb.customviewcollection.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes

/**
 * @author jhb
 * @date 2020/8/6
 */
val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = this.toFloat().dp

fun String.logE() {
    Log.e("jiang", this)
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