package com.example.zazulinsepr_1_2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabs()
        setupBottomNavigation()

        createNotificationChannels()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // обработка результата
        }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    companion object {
        const val CHANNEL_ID_DEFAULT = "default_channel"
        const val CHANNEL_ID_CHAT = "chat_channel"
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultChannel = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                "Основные уведомления",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Канал для основных уведомлений приложения" }

            val chatChannel = NotificationChannel(
                CHANNEL_ID_CHAT,
                "Сообщения",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Уведомления о новых сообщениях" }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(listOf(defaultChannel, chatChannel))
        }
    }

    private fun setupTabs() {
        // Создаем адаптер
        val adapter = ViewPagerAdapter(this)

        findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.view_pager).adapter = adapter
        // Связываем TabLayout с ViewPager2
        val tabLayout =
            findViewById<com.google.android.material.tabs.TabLayout>(R.id.tab_layout)
        val viewPager =
            findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.view_pager)

        TabLayoutMediator(tabLayout, viewPager) { tab, position -> tab.text = when (position) {
                0 -> "Новости"
                1 -> "Погода"
                2 -> "Спорт"
                else -> null
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab) {
                findViewById<android.widget.FrameLayout>(R.id.fragment_container).visibility = View.GONE
                viewPager.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
        })

    }
    private fun setupBottomNavigation() {
        val bottomNav =
            findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        val fragmentContainer =
            findViewById<android.widget.FrameLayout>(R.id.fragment_container)
        val viewPager =
            findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.view_pager)
        val tabLayout =
            findViewById<com.google.android.material.tabs.TabLayout>(R.id.tab_layout)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Показываем вкладки и ViewPager
                    viewPager.visibility = View.GONE
                    tabLayout.visibility = View.VISIBLE
                    fragmentContainer.visibility = View.VISIBLE

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    tabLayout.selectTab(null)
                    true
                }
                R.id.nav_notes, R.id.nav_about -> {
                    // Прячем вкладки
                    viewPager.visibility = View.GONE
                    tabLayout.visibility = View.GONE
                    fragmentContainer.visibility = View.VISIBLE

                    val fragment: Fragment = when (item.itemId) {
                        R.id.nav_notes -> NotesFragment()
                        R.id.nav_about -> AboutFragment()
                        else -> HomeFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
        // При старте сразу Home
        bottomNav.selectedItemId = R.id.nav_home
    }
}