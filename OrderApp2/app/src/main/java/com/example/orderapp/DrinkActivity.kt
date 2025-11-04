package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class DrinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        val rbTea = findViewById<RadioButton>(R.id.rbTea)
        val rbCola = findViewById<RadioButton>(R.id.rbCola)
        val rbJuice = findViewById<RadioButton>(R.id.rbJuice)

        findViewById<Button>(R.id.btnConfirmDrink).setOnClickListener {
            val drink = when {
                rbTea.isChecked -> "紅茶"
                rbCola.isChecked -> "可樂"
                rbJuice.isChecked -> "果汁"
                else -> null
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("drink", drink)
            startActivity(intent)
        }
    }
}