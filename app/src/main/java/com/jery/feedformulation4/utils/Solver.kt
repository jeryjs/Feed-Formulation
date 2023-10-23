package com.jery.feedformulation4.utils

class Solver(private val feeds: List<List<Double>>, private val costs: List<Double>, private val DM: Double, private val CP: Double, private val TDN: Double) {
    private var total_dm = DM
    private var total_cp = CP
    private var total_tdn = TDN
    private var error = false
    private var ans = 0.0

    private fun operate(t: Array<DoubleArray>, s: Double, o: Double, f: Double, n: Double) { }

    private fun primal(t: Array<DoubleArray>, s: Double, f: Double, n: Double, l: Double) { }

    private fun dual(t: Array<DoubleArray>, s: Double, f: Double, n: Double, l: Double) { }

    private fun simplex(t: Array<DoubleArray>, s: Double, f: Double, n: Double) { }

    private fun result(t: Array<DoubleArray>, o: Array<Double>, f: Double, n: Double) { }

    private fun createEquations(t: List<List<Double>>, o: List<Double>, f: List<Double>) { }

    fun solve(t: List<List<Double>>) {
        val l = t
        val s = createEquations(feeds, listOf(DM, CP, TDN), costs)
    }
}
