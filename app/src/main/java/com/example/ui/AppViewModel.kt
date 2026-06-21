package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.Attendance
import com.example.data.Fee
import com.example.data.Notice
import com.example.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    // Session State
    private val _loggedInUserId = MutableStateFlow<Int?>(null)
    val loggedInUserId: StateFlow<Int?> = _loggedInUserId.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database)
        
        // Insert sample notices on first run (for demonstration)
        viewModelScope.launch {
            repository.insertNotice(Notice(title = "Welcome", content = "Welcome to Krishna Library App!", timestamp = System.currentTimeMillis(), type = "Info"))
            repository.insertNotice(Notice(title = "Fee Reminder", content = "Please clear due fees before the end of the month.", timestamp = System.currentTimeMillis() - 86400000, type = "Alert"))
        }
    }

    // Current User
    val currentUserFlow: StateFlow<User?> = _loggedInUserId.flatMapLatest { id ->
        if (id != null) {
            repository.getUserFlow(id)
        } else {
            MutableStateFlow(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current User Attendance
    val currentUserAttendanceFlow: StateFlow<List<Attendance>> = _loggedInUserId.flatMapLatest { id ->
        if (id != null) {
            repository.getAttendanceFlow(id)
        } else {
            MutableStateFlow(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current User Fees
    val currentUserFeesFlow: StateFlow<List<Fee>> = _loggedInUserId.flatMapLatest { id ->
        if (id != null) {
            repository.getFeesFlow(id)
        } else {
            MutableStateFlow(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Notices
    val allNoticesFlow: StateFlow<List<Notice>> = repository.getNoticesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun login(username: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                 _loggedInUserId.value = user.id
            } else {
                // Return an error in a real app or create a mock user for this demo
            }
        }
    }

    fun logout() {
        _loggedInUserId.value = null
    }

    fun addMockUser() {
        viewModelScope.launch {
            val user = repository.getUserByUsername("testuser")
            if (user == null) {
               val newUserId = repository.insertUser(User(
                   fullName = "Test User",
                   username = "testuser",
                   mobile = "9876543210",
                   address = "123 Main St",
                   pinCode = "123456",
                   status = "Approved",
                   seatNo = "A-12"
               ))
               
               // Add a mock fee
               repository.insertFee(Fee(
                   userId = newUserId.toInt(),
                   amount = 500,
                   dueDate = "2024-06-30",
                   status = "Unpaid",
                   paidDate = null
               ))
            }
        }
    }

    fun submitRegistration(user: User, callback: (Boolean) -> Unit) {
         viewModelScope.launch {
             val existingUser = repository.getUserByUsername(user.username)
             if (existingUser == null) {
                 repository.insertUser(user)
                 callback(true)
             } else {
                 callback(false)
             }
         }
    }

    fun markAttendance(callback: (Boolean, String) -> Unit) {
        val currentUserId = _loggedInUserId.value
        if (currentUserId == null) {
            callback(false, "User not logged in.")
            return
        }

        viewModelScope.launch {
            val user = currentUserFlow.value
            if (user?.status != "Approved") {
                 callback(false, "Account not approved for attendance.")
                 return@launch
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val currentTime = timeFormat.format(Date())

            val existing = repository.getAttendanceByDate(currentUserId, currentDate)
            if (existing != null) {
                callback(false, "Attendance already marked for today.")
            } else {
                repository.insertAttendance(
                    Attendance(
                        userId = currentUserId,
                        date = currentDate,
                        time = currentTime,
                        status = "Present",
                        method = "QR"
                    )
                )
                callback(true, "Attendance Marked Successfully")
            }
        }
    }
}
