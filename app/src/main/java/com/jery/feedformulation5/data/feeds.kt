package com.jery.feedformulation5.data

data class Feed(
    val id: String,
    val name: String,
    var cost: String,
    val checked: Boolean,
    val type: String,
    val details: List<Double>
)
