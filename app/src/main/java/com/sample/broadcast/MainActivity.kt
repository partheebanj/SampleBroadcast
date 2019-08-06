package com.sample.broadcast

import android.content.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val CONNECTIVITY_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE"
    private var audioCount = 0
    private var imageCount = 0
    private var saveStatus = false

    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("Broadcast",Context.MODE_PRIVATE)
        editor = sharedPreference?.edit()

        displayAudioCount()
        displayImageCount()
        displaySaveStatus()

        broadcast1Btn?.setOnClickListener(this)
        broadcast2Btn?.setOnClickListener(this)
        broadcast3Btn?.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkUpdateReceiver, IntentFilter(CONNECTIVITY_CHANGED))

        val notificationArray = arrayOf(NotificationSystem.NotificationType.SaveStatus,
            NotificationSystem.NotificationType.ImageDownloaded,
            NotificationSystem.NotificationType.AudioDownloaded)

        NotificationSystem.addObserver(this, mReceivers, notificationArray)
    }

    override fun onPause() {
        super.onPause()
        NotificationSystem.removeObserver(this, mReceivers)
        unregisterReceiver(networkUpdateReceiver)
    }

    override fun onClick(view: View?) {
        when(view) {
            broadcast1Btn -> addAudioCount()
            broadcast2Btn -> addImageCount()
            broadcast3Btn -> changeSaveStatus()
        }
    }


    //
    private fun addAudioCount() {
        val audioDownloadNotification = {
            val params = HashMap<String, Value>()
            NotificationSystem.postNotification(this, NotificationSystem.NotificationType.AudioDownloaded, params)
        }
        audioDownloadNotification()
    }

    private fun addImageCount() {
        val imageDownloadNotification = {
            val params = HashMap<String, Value>()
            NotificationSystem.postNotification(this, NotificationSystem.NotificationType.ImageDownloaded, params)
        }
        imageDownloadNotification()
    }

    private fun changeSaveStatus() {
        val saveStatusNotification = {
            val params = HashMap<String, Value>()
            NotificationSystem.postNotification(this, NotificationSystem.NotificationType.SaveStatus, params)
        }
        saveStatusNotification()
    }


    private fun displayAudioCount() {
        // Retrieving and displaying values from shared preferences
        val displayText = "Audio downloaded = ${sharedPreference.getInt("audioCount", 0)}"
        broadcast1Tv.text = displayText
    }

    private fun displayImageCount() {
        // Retrieving and displaying values from shared preferences
        val displayText = "Image downloaded = ${sharedPreference.getString("imageCount", "0")}"
        broadcast2Tv.text = displayText
    }

    private fun displaySaveStatus() {
        // Retrieving and displaying values from shared preferences
        val displayText = "Save status = ${sharedPreference.getBoolean("saveStatus", false)}"
        broadcast3Tv.text = displayText
    }



    // Broadcast receiver
    private var mReceivers = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                NotificationSystem.NotificationType.AudioDownloaded.name ->{
                    audioCount++
                    // Saving int to shared preference
                    editor.putInt("audioCount", audioCount)
                    editor.apply()


                 displayAudioCount()

                }
                NotificationSystem.NotificationType.ImageDownloaded.name -> {
                    imageCount++
                    // Saving int to shared preference
                    editor.putString("imageCount", imageCount?.toString())
                    editor.apply()


                    displayImageCount()
                }
                NotificationSystem.NotificationType.SaveStatus.name -> {
                    // Changing status to the opposite of the current value
                    saveStatus = !saveStatus
                    editor.putBoolean("saveStatus", saveStatus)
                    editor.apply()

                    displaySaveStatus()
                }
            }
        }
    }


    private val networkUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkUtil.isConnected(this@MainActivity)) {
                internetTv.text = "Internet is connected"
            } else {
                internetTv.text = "Internet is not connected"
            }
        }
    }


}
