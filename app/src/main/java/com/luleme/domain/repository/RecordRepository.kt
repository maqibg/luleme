package com.luleme.domain.repository

import com.luleme.domain.model.Record

interface RecordRepository {
    suspend fun getTodayRecords(): List<Record>
    suspend fun getRecordsBetween(startDate: String, endDate: String): List<Record>
    suspend fun addRecord(note: String? = null)
    suspend fun addRecords(date: String, count: Int)
    suspend fun clearAll()
    suspend fun getAllRecords(): List<Record>
    suspend fun importRecords(records: List<Record>)
    suspend fun deleteRecord(id: Long)
    suspend fun deleteRecordsByDate(date: String)
    suspend fun getRecordsByDate(date: String): List<Record>
}
