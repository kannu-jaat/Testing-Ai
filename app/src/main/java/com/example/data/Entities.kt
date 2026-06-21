package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val username: String,
    val mobile: String,
    val address: String,
    val pinCode: String,
    val status: String = "Pending", // "Pending", "Approved", "Blocked"
    val seatNo: String = "Not Assigned",
    val registrationDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String, // YYYY-MM-DD
    val time: String, // HH:MM AM/PM
    val status: String, // Present, Absent
    val method: String // QR, Manual
)

@Entity(tableName = "fees")
data class Fee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val amount: Int,
    val dueDate: String,
    val status: String, // Paid, Unpaid, Pending Approval
    val paidDate: String?
)

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val type: String,
    val isRead: Boolean = false
)
