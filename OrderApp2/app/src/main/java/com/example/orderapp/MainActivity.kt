package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvOrder: TextView
    private var mainDish: String? = null
    private var sideDish: String? = null
    private var drink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvOrder = findViewById(R.id.tvOrder)

        findViewById<Button>(R.id.btnMainDish).setOnClickListener {
            startActivity(Intent(this, MainDishActivity::class.java))
        }
        findViewById<Button>(R.id.btnSideDish).setOnClickListener {
            startActivity(Intent(this, SideDishActivity::class.java))
        }
        findViewById<Button>(R.id.btnDrink).setOnClickListener {
            startActivity(Intent(this, DrinkActivity::class.java))
        }
        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("mainDish", mainDish)
            intent.putExtra("sideDish", sideDish)
            intent.putExtra("drink", drink)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 重新讀取資料（例如從 sharedPreferences 或 static object）
        tvOrder.text = "主餐：${mainDish ?: "未選"}\n副餐：${sideDish ?: "未選"}\n飲料：${drink ?: "未選"}"
    }
}