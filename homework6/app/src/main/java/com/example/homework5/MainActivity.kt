package com.example.homework5

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val viewModel: WorkViewModel by viewModels()
    private var player: MediaPlayer? = null // 播放器宣告

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false

        // 監聽狀態變化
        viewModel.status.observe(this) { status ->
            when (status) {
                "準備中" -> {
                    viewPager.setCurrentItem(1, true)
                    startMusic()
                }
                "工作中" -> {
                    viewPager.setCurrentItem(2, true)
                    startMusic()
                }
                "已完成" -> {
                    stopMusic()
                }
                "已取消" -> {
                    stopMusic()
                    viewPager.setCurrentItem(0, true)
                }
            }
        }

        // 監聽進度變化
        viewModel.progress.observe(this) { progress ->
            when {
                progress == 0 -> {
                    if (viewModel.status.value == "已取消") {
                        stopMusic()
                        viewPager.setCurrentItem(0, true)
                    }
                }
                progress in 1..99 -> {
                    startMusic()
                    viewPager.setCurrentItem(1, true)
                }
                progress == 100 -> {
                    stopMusic()
                }
            }
        }
    }

    private fun startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.background_music)
            player?.isLooping = true
            player?.start()
        } else if (player?.isPlaying == false) {
            player?.start()
        }
    }

    private fun stopMusic() {
        player?.pause()
        player?.seekTo(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}