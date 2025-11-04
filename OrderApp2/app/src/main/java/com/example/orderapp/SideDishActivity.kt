package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class SideDishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_dish)

        val cbSalad = findViewById<CheckBox>(R.id.cbSalad)
        val cbSoup = findViewById<CheckBox>(R.id.cbSoup)
        val cbFries = findViewById<CheckBox>(R.id.cbFries)

        findViewById<Button>(R.id.btnConfirmSide).setOnClickListener {
            val sides = mutableListOf<String>()
            if (cbSalad.isChecked) sides.add("沙拉")
            if (cbSoup.isChecked) sides.add("湯")
            if (cbFries.isChecked) sides.add("薯條")

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("sideDish", sides.joinToString(", "))
            startActivity(intent)
        }
    }
}