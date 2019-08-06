package com.sample.broadcast

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkUtil {

    fun isNetworkAvailable(context: Context?): Boolean {
        return if (context == null) {
            false
        } else {
            isConnected(context)
        }
    }

    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return (info != null && info!!.isConnected)
    }
}