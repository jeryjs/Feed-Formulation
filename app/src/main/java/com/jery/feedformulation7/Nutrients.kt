package com.jery.feedformulation7

import kotlin.math.pow

class Nutrients(private var BW: Int, private var MY: Int, private var MF: Int, private var P: Int) {

    var DM = 0.0
    var CP = 0.0
    var TDN = 0.0

    companion object {
        private var nutrients: Nutrients? = null
        var type = 0

        fun getInstance(): Nutrients? {
            if (nutrients == null) nutrients = Nutrients(0,0,0,0)
            return nutrients
        }

        fun setInstance(n: Nutrients?) {
            nutrients = n
        }
    }

    fun calculateNutrients() {
        if (type == 0) {
            DM = (BW * 0.0216) + (MY * (MF * 0.063 + 0.259)) + (0.14 * P + 0.01)
            if (P == 0) {
                CP = (BW.toDouble().pow(0.75)) * 4.87 + MY * 96
                TDN = ((-0.093 + (0.038 * (BW.toDouble().pow(0.75))) * 1000) + (MY * ((42 * MF) + 162)))
            } else {
                CP = (BW.toDouble().pow(0.75)) * 4.87 + MY * 96 + ((P * 47) - 113)
                TDN = ((-0.093 + (0.038 * (BW.toDouble().pow(0.75)))) * 1000) + ((MY * ((42 * MF) + 162))
                        + ((100 * P) + 40))
            }
        }
    }
}