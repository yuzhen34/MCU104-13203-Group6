package com.example.lab15

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 自訂建構子並繼承 SQLiteOpenHelper 類別
class MyDBHelper(
    context: Context,
    name: String = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = VERSION
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val DB_NAME = "carDatabase"   // 資料庫名稱
        private const val VERSION = 1               // 資料庫版本
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 建立 cars 資料表（舊車商）
        db.execSQL(
            """
            CREATE TABLE cars (
                brand TEXT NOT NULL,
                year INTEGER NOT NULL,
                price INTEGER NOT NULL,
                PRIMARY KEY (brand, year)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 升級資料庫版本時，刪除舊資料表並重建
        db.execSQL("DROP TABLE IF EXISTS cars")
        onCreate(db)
    }
}