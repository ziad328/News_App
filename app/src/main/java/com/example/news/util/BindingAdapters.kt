package com.example.news.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["app:url", "app:placeholder"], requireAll = false)
fun bindImage(imageView: ImageView, url: String?, placeholder: Drawable?) {
    Glide.with(imageView)
        .load(url)
        .placeholder(placeholder)
        .into(imageView)
}