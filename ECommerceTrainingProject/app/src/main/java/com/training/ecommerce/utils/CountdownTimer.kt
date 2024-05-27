package com.training.ecommerce.utils

import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class CountdownTimer(
    private val futureDate: Date,
    private val onTick: (hours: Int, minutes: Int, seconds: Int) -> Unit
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    private var timerJob: Job? = null

    fun start() {
        timerJob?.cancel()
        timerJob = launch {
            val futureTime = futureDate.time
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                if (currentTime >= futureTime) {
                    onTick(0, 0, 0)
                    break
                }
                val diff = futureTime - currentTime
                val hours = (diff / (1000 * 60 * 60)).toInt()
                val minutes = ((diff / (1000 * 60)) % 60).toInt()
                val seconds = (diff / 1000 % 60).toInt()
                onTick(hours, minutes, seconds)
                delay(1000)
            }
        }
    }

    fun stop() {
        job.cancel()
        timerJob?.cancel()
    }

}

