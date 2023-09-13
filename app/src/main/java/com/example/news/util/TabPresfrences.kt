package com.example.news.util

import android.content.Context
import android.content.SharedPreferences

class TabPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tab_prefs", Context.MODE_PRIVATE)

    fun saveSelectedTab(tabIndex: Int) {
        sharedPreferences.edit().putInt("selected_tab", tabIndex).apply()
    }

    fun getSelectedTab(): Int {
        return sharedPreferences.getInt(
            "selected_tab",
            0
        ) // Default to the first tab if no tab has been selected before
    }
}
