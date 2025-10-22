package com.example.zazulinsepr_1_2

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Кнопка для теста уведомления
        view.findViewById<Button>(R.id.btnShowChatNotification)?.setOnClickListener {
            showChatNotification()
        }
    }

    private fun showChatNotification() {
        val context = requireContext()
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        // Открытие приложения
        val openAppIntent = Intent(context, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Действие "Прочитано"
        val markReadIntent = Intent(context, ChatReceiver::class.java).apply {
            action = ChatReceiver.ACTION_MARK_READ
        }
        val markReadPendingIntent = PendingIntent.getBroadcast(
            context, 1, markReadIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Действие "Ответить"
        val replyIntent = Intent(context, ChatReceiver::class.java).apply {
            action = ChatReceiver.ACTION_REPLY
        }
        val replyPendingIntent = PendingIntent.getBroadcast(
            context, 2, replyIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Создаем уведомление
        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID_CHAT)
            .setSmallIcon(android.R.drawable.sym_action_chat)
            .setContentTitle("Новое сообщение")
            .setContentText("Привет! Ты сделал лабораторную?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(android.R.drawable.ic_menu_send, "Ответить", replyPendingIntent)
            .addAction(android.R.drawable.ic_menu_delete, "Прочитано", markReadPendingIntent)
            .build()

        notificationManager.notify(2001, notification)
    }
}
