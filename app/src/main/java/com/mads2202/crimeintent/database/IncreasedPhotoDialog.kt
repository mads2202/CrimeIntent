package com.mads2202.crimeintent.database

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.mads2202.crimeintent.R

class IncreasedPhotoDialog:DialogFragment() {
    lateinit var mImageView: ImageView
    companion object{
        val BITMAP_ARG="BitMap"
        fun newInstance(bitmap: Bitmap):IncreasedPhotoDialog{
            val args=Bundle()
            args.putParcelable(BITMAP_ARG,bitmap)
            val increasedPhotoDialog=IncreasedPhotoDialog()
            increasedPhotoDialog.arguments=args
            return increasedPhotoDialog
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view=LayoutInflater.from(activity).inflate(R.layout.crime_photo,null)
        mImageView=view!!.findViewById(R.id.photo)
        mImageView.setImageBitmap(arguments!!.getParcelable(BITMAP_ARG))
        return AlertDialog.Builder(activity).setTitle("Incrased photo").setView(view).create()
    }
}