package com.example.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var txtShow: TextView
    private lateinit var btnClear: Button
    private lateinit var numberButtons: Array<Button?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //

        txtShow = findViewById(R.id.txtShow)
        btnClear = findViewById(R.id.btnClear)

        val ids = arrayOf(
            R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree,
            R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven,
            R.id.btnEight, R.id.btnNine
        )

        numberButtons = arrayOfNulls(10)

        val myListener = View.OnClickListener { v ->
            val b = v as Button
            txtShow.text = txtShow.text.toString() + b.text.toString()
        }

        for (i in ids.indices) {
            numberButtons[i] = findViewById(ids[i])
            numberButtons[i]?.setOnClickListener(myListener)
        }

        btnClear.setOnClickListener {
            txtShow.text = ""
        }
    }
}