package com.example.healthsystem.repository

import com.example.healthsystem.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HealthRepository(private val dbHelper: DatabaseHelper) {
    suspend fun register(user: User) = withContext(Dispatchers.IO) { dbHelper.registerUser(user) }
    suspend fun login(email: String, pin: String): User? = withContext(Dispatchers.IO) { dbHelper.login(email, pin) }
    suspend fun addAppointment(appointment: Appointment) = withContext(Dispatchers.IO) { dbHelper.insertAppointment(appointment) }
    suspend fun getAppointments(patientId: Int): List<Appointment> = withContext(Dispatchers.IO) { dbHelper.getUserAppointments(patientId) }
    suspend fun deleteAppointment(id: Int) = withContext(Dispatchers.IO) { dbHelper.deleteAppointment(id) }

    suspend fun updateAppointment(id: Int, newDate: String, newTime: String) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            dbHelper.updateAppointment(id, newDate, newTime)
        }
    }
}
