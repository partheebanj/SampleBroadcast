package com.sample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.Serializable
import java.util.*

object NotificationSystem {

    /**
     * Mock scenarios for notifications
     */
    enum class NotificationType {
        ImageDownloaded,
        AudioDownloaded,
        SaveStatus
    }

    /**
     * Register single broadcast receiver to the activity
     */
    private fun addObserver(context: Context, receiver: BroadcastReceiver, notificationType: NotificationType) {
        val intentFilter = IntentFilter(notificationType.name)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)
    }

    /**
     * Register multiple broadcast receiver to the activity
     */
    fun addObserver(context: Context, receiver: BroadcastReceiver, notificationTypes: Array<NotificationType>) {
        for (notificationType in notificationTypes) {
            this.addObserver(context, receiver, notificationType)
        }
    }

    /**
     * De-register broadcast receiver to the activity
     */
    fun removeObserver(context: Context, receiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }

    /**
     * Post a broadcast event
     */
    fun postNotification(context: Context, notificationType: NotificationType, params: HashMap<String,Value>) {

        val intent = Intent(notificationType.name)
        for (entry in params.entries) {
            intent.putExtra(entry.key, entry.value)
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}

/**
 * Serialized class used to carry the data for broadcast events
 */
class Value: Serializable {
    private var value: Any? = null

    constructor(value: Any) {
        this.value = value
    }

    fun getValue(): Any {
        return value ?: ""
    }

    override fun toString(): String {
        return if (value != null) "$value" else ""
    }

}