package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val database: AppDatabase) {
    val userDao = database.userDao()
    val attendanceDao = database.attendanceDao()
    val feeDao = database.feeDao()
    val noticeDao = database.noticeDao()

    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    fun getUserFlow(userId: Int): Flow<User?> = userDao.getUserFlow(userId)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    fun getAttendanceFlow(userId: Int): Flow<List<Attendance>> = attendanceDao.getAttendanceFlow(userId)
    suspend fun getAttendanceByDate(userId: Int, date: String): Attendance? = attendanceDao.getAttendanceByDate(userId, date)
    suspend fun insertAttendance(attendance: Attendance) = attendanceDao.insertAttendance(attendance)

    fun getFeesFlow(userId: Int): Flow<List<Fee>> = feeDao.getFeesFlow(userId)
    suspend fun insertFee(fee: Fee) = feeDao.insertFee(fee)

    fun getNoticesFlow(): Flow<List<Notice>> = noticeDao.getNoticesFlow()
    suspend fun insertNotice(notice: Notice) = noticeDao.insertNotice(notice)
    suspend fun updateNotice(notice: Notice) = noticeDao.updateNotice(notice)
}
