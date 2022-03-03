package com.example.mediastoreexample

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setImage")
fun setImageWithSolaroid(imageView: ImageView, uri: Uri?) {
    uri?.let {
        Glide.with(imageView.context)
            .load(it)
            .into(imageView)
    }
}