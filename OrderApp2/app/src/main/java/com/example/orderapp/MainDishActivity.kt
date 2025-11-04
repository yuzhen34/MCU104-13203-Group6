package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class MainDishActivity : AppCompatActivity() {
    private var selectedMain: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dish)

        val btnConfirm = findViewById<Button>(R.id.btnConfirmMain)
        val rbChicken = findViewById<RadioButton>(R.id.rbChicken)
        val rbBeef = findViewById<RadioButton>(R.id.rbBeef)
        val rbFish = findViewById<RadioButton>(R.id.rbFish)

        btnConfirm.setOnClickListener {
            selectedMain = when {
                rbChicken.isChecked -> "雞腿飯"
                rbBeef.isChecked -> "牛排飯"
                rbFish.isChecked -> "魚排飯"
                else -> null
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mainDish", selectedMain)
            startActivity(intent)
        }
    }
}