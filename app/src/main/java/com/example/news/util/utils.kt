package com.example.news.util

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

fun getCurrentLanguage(context: Context): String {
    val configuration = context.resources.configuration
    return configuration.locales[0].language
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
