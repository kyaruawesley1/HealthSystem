package com.example.healthsystem.data

data class User(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val pin: String
)

data class Appointment(
    val id: Int = 0,
    val patientId: Int,
    val serviceType: String,
    val date: String,
    val time: String
)