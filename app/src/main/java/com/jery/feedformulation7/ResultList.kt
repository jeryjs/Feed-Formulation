package com.jery.feedformulation7

import kotlin.math.roundToInt


class ResultList(var name: String, var cost: Double, var amount: Double) {

    fun getCostString(): String {
        return "" + cost
    }

    fun setCostString(cost: Double) {
        this.cost = cost
    }

    fun setAmountString(amount: Double) {
        this.amount = amount
    }

    fun getAmountString(): String {
        return "" + amount
    }

    val total: String
        get() = "" + (cost * amount * 100.0).roundToInt() / 100.0
    val totalCost: Double
        get() = (cost * amount * 100.0).roundToInt() / 100.0
}

