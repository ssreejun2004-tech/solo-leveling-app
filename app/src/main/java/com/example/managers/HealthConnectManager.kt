package com.example.managers

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.temporal.ChronoUnit

class HealthConnectManager(private val context: Context) {

    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    private val _steps = MutableStateFlow(0L)
    val steps: StateFlow<Long> = _steps.asStateFlow()
    
    private val _heartRate = MutableStateFlow(0L)
    val heartRate: StateFlow<Long> = _heartRate.asStateFlow()

    fun isHealthConnectAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }

    suspend fun syncData() {
        if (!isHealthConnectAvailable()) return
        
        try {
            val startTime = Instant.now().truncatedTo(ChronoUnit.DAYS)
            val endTime = Instant.now()
            
            // Read Steps
            val stepsRequest = ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val stepsResponse = healthConnectClient.readRecords(stepsRequest)
            var totalSteps = 0L
            for (record in stepsResponse.records) {
                totalSteps += record.count
            }
            _steps.value = totalSteps
            
            // Read Heart Rate
            val hrRequest = ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            val hrResponse = healthConnectClient.readRecords(hrRequest)
            var maxHr = 0L
            for (record in hrResponse.records) {
                for (sample in record.samples) {
                    if (sample.beatsPerMinute > maxHr) {
                        maxHr = sample.beatsPerMinute
                    }
                }
            }
            _heartRate.value = maxHr
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
