package com.example.lab15

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase

        // 宣告Adapter並連結ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter

        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }

    private fun setListener() {
        val edBrand = findViewById<EditText>(R.id.edBrand)
        val edYear = findViewById<EditText>(R.id.edYear)
        val edPrice = findViewById<EditText>(R.id.edPrice)

        findViewById<Button>(R.id.btnInsert).setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val year = edYear.text.toString().toIntOrNull()
            val price = edPrice.text.toString().toIntOrNull()

            if (brand.isBlank() || year == null || price == null) {
                showToast("欄位請勿留空，年份/價格需為數字")
                return@setOnClickListener
            }

            try {
                dbrw.execSQL(
                    "INSERT INTO cars(brand, year, price) VALUES(?,?,?)",
                    arrayOf(brand, year, price)
                )
                showToast("新增：廠牌:$brand 年份:$year 價格:$price")
                cleanEditText()
            } catch (e: Exception) {
                showToast("新增失敗：$e")
            }
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val year = edYear.text.toString().toIntOrNull()
            val price = edPrice.text.toString().toIntOrNull()

            if (brand.isBlank() || year == null || price == null) {
                showToast("欄位請勿留空，年份/價格需為數字")
                return@setOnClickListener
            }

            try {
                // 以「廠牌 + 年份」當作定位條件，更新價格
                dbrw.execSQL(
                    "UPDATE cars SET price = ? WHERE brand = ? AND year = ?",
                    arrayOf(price, brand, year)
                )
                showToast("更新：廠牌:$brand 年份:$year 新價格:$price")
                cleanEditText()
            } catch (e: Exception) {
                showToast("更新失敗：$e")
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val brand = edBrand.text.toString().trim()
            val year = edYear.text.toString().toIntOrNull()

            if (brand.isBlank() || year == null) {
                showToast("廠牌不可空，年份需為數字")
                return@setOnClickListener
            }

            try {
                // 刪除符合「廠牌 + 年份」的車
                dbrw.execSQL(
                    "DELETE FROM cars WHERE brand = ? AND year = ?",
                    arrayOf(brand, year)
                )
                showToast("刪除：廠牌:$brand 年份:$year")
                cleanEditText()
            } catch (e: Exception) {
                showToast("刪除失敗：$e")
            }
        }

        findViewById<Button>(R.id.btnQuery).setOnClickListener {
            val brand = edBrand.text.toString().trim()

            val queryString: String
            val args: Array<String>?

            // 沒輸入廠牌 -> 查全部；有輸入廠牌 -> 查該廠牌
            if (brand.isBlank()) {
                queryString = "SELECT brand, year, price FROM cars"
                args = null
            } else {
                queryString = "SELECT brand, year, price FROM cars WHERE brand = ?"
                args = arrayOf(brand)
            }

            val c = dbrw.rawQuery(queryString, args)
            items.clear()
            showToast("共有 ${c.count} 筆資料")

            if (c.moveToFirst()) {
                do {
                    val b = c.getString(0)
                    val y = c.getInt(1)
                    val p = c.getInt(2)
                    items.add("廠牌:$b   年份:$y   價格:$p")
                } while (c.moveToNext())
            }

            adapter.notifyDataSetChanged()
            c.close()
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    private fun cleanEditText() {
        findViewById<EditText>(R.id.edBrand).setText("")
        findViewById<EditText>(R.id.edYear).setText("")
        findViewById<EditText>(R.id.edPrice).setText("")
    }
}