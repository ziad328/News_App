package com.example.news.util

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.showAlertDialog(
    message: String,
    posActionName: String? = null,
    posAction: DialogInterface.OnClickListener? = null,
    negActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null
): AlertDialog {

    val alertDialogBuilder = AlertDialog.Builder(requireContext())
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton(posActionName, posAction)
    alertDialogBuilder.setNegativeButton(negActionName, negAction)
    return alertDialogBuilder.show()
}

fun Activity.showAlertDialog(
    message: String,
    posActionName: String? = null,
    posAction: DialogInterface.OnClickListener? = null,
    negActionName: String? = null,
    negAction: DialogInterface.OnClickListener? = null
): AlertDialog {

    val alertDialogBuilder = AlertDialog.Builder(this)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton(posActionName, posAction)
    alertDialogBuilder.setNegativeButton(negActionName, negAction)
    return alertDialogBuilder.show()
}

fun Fragment.recreateActivity() {
    activity?.let {
        val intent = Intent(it, it::class.java)
        it.startActivity(intent)
        it.finish()
    }
}

