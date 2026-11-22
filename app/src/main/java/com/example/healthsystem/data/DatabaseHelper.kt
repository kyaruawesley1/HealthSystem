package com.example.healthsystem.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "health_wellness.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, fullName TEXT, email TEXT, pin TEXT)")
        db.execSQL("CREATE TABLE appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, patientId INTEGER, serviceType TEXT, date TEXT, time TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS appointments")
        onCreate(db)
    }

    fun registerUser(user: User) {
        val values = ContentValues().apply {
            put("fullName", user.fullName)
            put("email", user.email)
            put("pin", user.pin)
        }
        writableDatabase.insert("users", null, values)
    }

    fun login(email: String, pin: String): User? {
        val cursor = readableDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND pin = ?", arrayOf(email, pin))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
        }
        cursor.close()
        return user
    }

    fun insertAppointment(appt: Appointment) {
        val values = ContentValues().apply {
            put("patientId", appt.patientId)
            put("serviceType", appt.serviceType)
            put("date", appt.date)
            put("time", appt.time)
        }
        writableDatabase.insert("appointments", null, values)
    }

    fun getUserAppointments(patientId: Int): List<Appointment> {
        val list = mutableListOf<Appointment>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM appointments WHERE patientId = ? ORDER BY id DESC", arrayOf(patientId.toString()))
        while (cursor.moveToNext()) {
            list.add(Appointment(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
        }
        cursor.close()
        return list
    }

    fun deleteAppointment(id: Int) {
        writableDatabase.delete("appointments", "id = ?", arrayOf(id.toString()))
    }

    fun updateAppointment(id: Int, newDate: String, newTime: String): Boolean {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("date", newDate)
            put("time", newTime)
        }
        val result = db.update("appointments", values, "id=?", arrayOf(id.toString()))
        return result != -1
    }
}