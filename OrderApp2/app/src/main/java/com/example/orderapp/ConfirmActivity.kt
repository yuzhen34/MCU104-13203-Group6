package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ConfirmActivity : AppCompatActivity() {
    private var mainDish: String? = null
    private var sideDish: String? = null
    private var drink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        mainDish = intent.getStringExtra("mainDish")
        sideDish = intent.getStringExtra("sideDish")
        drink = intent.getStringExtra("drink")

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            if (mainDish.isNullOrEmpty() || sideDish.isNullOrEmpty() || drink.isNullOrEmpty()) {
                Toast.makeText(this, "請選擇主餐、副餐和飲料！", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("提交訂單")
                .setMessage("主餐：$mainDish\n副餐：$sideDish\n飲料：$drink\n\n是否提交？")
                .setPositiveButton("提交") { d, _ ->
                    Toast.makeText(this, "訂單已送出！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }
}