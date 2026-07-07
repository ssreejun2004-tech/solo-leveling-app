package com.example.managers

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationTrackerManager(private val context: Context) {
    private val _distanceMeters = MutableStateFlow(0f)
    val distanceMeters: StateFlow<Float> = _distanceMeters.asStateFlow()

    fun startTracking() {
        // AppOps restricts real location monitoring in this environment.
        // Location will be tracked via manual simulation inputs.
    }

    fun stopTracking() {
        // No-op
    }
    
    fun addDistance(meters: Float) {
        _distanceMeters.value += meters
    }
}
