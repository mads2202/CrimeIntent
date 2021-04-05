package com.mads2202.crimeintent

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

class PictureUtils {
    companion object{
        fun getScaledBitMap(path: String, destWidth: Int, destHeight: Int):Bitmap{
            var options=BitmapFactory.Options()
            options.inJustDecodeBounds=true
            BitmapFactory.decodeFile(path, options)
            var srcWidth=options.outWidth
            var srcHeight=options.outHeight
            var inSampleSize=1
            if(srcHeight>destHeight || srcWidth>destWidth){
                var heightScale=srcHeight/destHeight
                var widthScale=srcWidth/destWidth
                inSampleSize= Math.round((if (heightScale > widthScale) heightScale else widthScale).toDouble())
                    .toInt()
            }
            options=BitmapFactory.Options()
            options.inSampleSize=inSampleSize
            return BitmapFactory.decodeFile(path,options)

        }
        fun getScaledBitMap(path:String, activity:Activity):Bitmap{
            var size:Point= Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitMap(path,size.x,size.y)
        }
    }
}