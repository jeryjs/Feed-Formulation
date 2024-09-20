package com.jery.feedformulation.model

data class SimplexResult(
    val totalDm: Double,
    val totalCp: Double,
    val totalTdn: Double,
    val feedWeights: List<Pair<String, Double>>
)