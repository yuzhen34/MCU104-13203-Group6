package com.example.homework5

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val viewModel: WorkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false

        // 觀察狀態變化自動切換頁面
        viewModel.status.observe(this) { status ->
            when (status) {
                "準備中" -> viewPager.setCurrentItem(1, true) // 切換到 Progress 頁
                "工作中" -> viewPager.setCurrentItem(2, true) // 切換到 Result 頁
                // 完成時不要自動跳回，讓用戶手動返回或保持當前頁面
            }
        }

        // 觀察進度變化
        viewModel.progress.observe(this) { progress ->
            when {
                progress == 0 -> {
                    // 初始狀態或取消狀態，保持在 Start 頁
                    if (viewModel.status.value == "已取消") {
                        viewPager.setCurrentItem(0, true)
                    }
                }
                progress == 100 -> {
                    // 工作完成時，保持在當前頁面顯示完成訊息
                    // 不自動切換頁面，讓用戶看到完成狀態
                }
                progress > 0 && progress < 100 -> {
                    // 工作中確保在 Progress 頁面
                    viewPager.setCurrentItem(1, true)
                }
            }
        }
    }
}