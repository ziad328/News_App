package com.example.news.util

data class ViewError(
    var message: String? = null,
    var onTryAgainClickListener: OnTryAgainClickListener? = null
)

fun interface OnTryAgainClickListener {
    fun onTryAgainClick()
}
