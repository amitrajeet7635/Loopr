package com.loopr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val merchant: String,
    val logo: String,
    val color: String,
    val plan: String,
    val price: Double,
    val currency: String,
    val frequency: String,
    val nextDueDate: String
)
