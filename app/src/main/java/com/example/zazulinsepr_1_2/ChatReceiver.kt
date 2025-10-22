package com.example.zazulinsepr_1_2

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ChatReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REPLY = "REPLY_ACTION"
        const val ACTION_MARK_READ = "MARK_READ_ACTION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_REPLY -> handleReplyAction(context)
            ACTION_MARK_READ -> handleMarkAsReadAction(context)
        }
    }

    private fun handleReplyAction(context: Context) {
        // Имитация перехода в чат
        val appIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("open_fragment", "chat")
        }
        context.startActivity(appIntent)
        Toast.makeText(context, "Открытие чата...", Toast.LENGTH_SHORT).show()
    }

    private fun handleMarkAsReadAction(context: Context) {
        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(2001) // ID уведомления
        Toast.makeText(context, "Сообщение помечено как прочитанное", Toast.LENGTH_SHORT).show()
    }
}
