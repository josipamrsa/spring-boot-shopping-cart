package com.example.shoppingcart.request.time_period

import java.time.Instant

data class TimePeriodRequest(
    val startPeriod: Instant,
    val endPeriod: Instant
)