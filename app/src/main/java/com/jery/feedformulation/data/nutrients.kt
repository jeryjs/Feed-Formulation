package com.jery.feedformulation.data

import kotlin.math.pow
import kotlin.math.round

data class Nutrients (
    private var bw: Double,
    private var my: Double,
    private var mf: Double,
    private var pr: Int
) {
    var type = 0

    var dm = 0.0
    var cp = 0.0
    var tdn = 0.0
    companion object {
        private lateinit var nutrients: Nutrients
        var type = 0

        fun getInstance(): Nutrients {
            return if (::nutrients.isInitialized) nutrients else Nutrients(0.0,0.0,0.0,0)
        }
        fun setInstance(n: Nutrients) { nutrients = n }
    }

//    private fun calculateNutrients(bw: Double) {
//        if (type == 0) {
//            dm = (bw * 0.0216) + (my * (mf * 0.063 + 0.259)) + (0.14 * pr + 0.01)
//            if (pr == 0) {
//                cp = (bw.pow(0.75)) * 4.87 + my * 96
//                tdn = ((-0.093 + (0.038 * (bw.pow(0.75))) * 1000) + (my * ((42 * mf) + 162)))
//            } else {
//                cp = (bw.pow(0.75)) * 4.87 + my * 96 + ((pr * 47) - 113)
//                tdn = ((-0.093 + (0.038 * (bw.pow(0.75)))) * 1000) + ((my * ((42 * mf) + 162))
//                        + ((100 * pr) + 40))
//            }
//        }
//    }

    fun calculateNutrients(): List<Double> {
        val s = 100
        when (type) {
            0 -> {
                dm = (0.0216 * bw + my * (0.063 * mf + 0.259) + (0.14 * 0 + 0.01)).let { round(it*s)/s }
                if (pr == 0) {
                    cp = (4.87 * bw.pow(0.75) + 96 * my).let { round(it*s)/s }
                    tdn = (1e3 * (0.038 * bw.pow(0.75) - 0.093) + my * (42 * mf + 162)).let { round(it*s)/s }
                } else {
                    cp = (4.87 * bw.pow(0.75) + 96 * my + (47 * pr - 113)).let { round(it*s)/s }
                    tdn = (1e3 * (0.038 * bw.pow(0.75) - 0.093) + (my * (42 * mf + 162) + (100 * pr + 40))).let { round(it*s)/s }
                }
            }
            1 -> {
                dm = (0.0216 * bw + my * (0.0632 * mf + 0.2946) + (0.17 * pr - 0.17)).let { round(it*s)/s }
                cp = (4.87 * bw.pow(0.75) + 124 * my + (56.7 * pr - 194.2)).let { round(it*s)/s }
                tdn = (1e3 * (0.038 * bw.pow(0.75) - 0.093 + my * (0.2 + 0.04 * mf) + (0.1 + 0.1 * pr))).let { round(it*s)/s }
            }
        }
        return listOf(dm, cp, tdn)
    }
}