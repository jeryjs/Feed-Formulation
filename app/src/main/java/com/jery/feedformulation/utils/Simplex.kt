package com.jery.feedformulation.utils

import android.util.Log
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.data.Nutrients


class NoSolutionFoundException(message: String?) : java.lang.Exception(message)

@Suppress("PropertyName", "PrivatePropertyName", "FunctionName", "LocalVariableName")
class Simplex internal constructor() {
    private lateinit var nutrients: Nutrients
    private lateinit var A: Array<Array<Double>>
    private lateinit var M: IntArray
    private var m = 0
    private var n = 0
    lateinit var ans: Array<Double?>
    var total_dm = 0.0
    var total_cp = 0.0
    var total_tdn = 0.0
    private var count = 0
    private var threshold = 2000

    // Entry point into the simplex solver
    fun solve(feedsFeed: List<Feed>, feedsList: List<Int>) {
        feeds = feedsFeed
        println("Selected Feeds: $feeds")
        nutrients = Nutrients.getInstance()
        FeedValues = feeds.map { feed -> feed.details }.toList()    // Converting to same format as FeedValues (Array<Array<Double>>)
//        percent = feeds.map { feed -> feed.percentage[0]!! }.toTypedArray()     // Converting to same format as percent (Array<Double>)
        CreateEquations()
        Log.v("Nutrients", "${nutrients.dm} -- ${nutrients.cp} -- ${nutrients.tdn}")

        simplex()
        Result()

        for ((i, f) in feeds.withIndex()) {
            total_dm += f.details[0] * ans[i]!!
            total_cp += f.details[1] * ans[i]!!
            total_tdn += f.details[2] * ans[i]!!
        }
    }

    private fun Operate(a: Int, b: Int) {
        var p = 0.0
        val B = Array(m + 1) { Array(n + 1) { 0.0 } }
        for (i in 0 until m + 1) for (j in 0 until n + 1) B[i][j] = A[i][j]
        p = B[a][b]
        A[a][b] = 1 / p
        for (i in 0 until m + 1) {
            if (i != a) {
                A[i][b] = -1 * B[i][b] / p
            }
        }
        for (j in 0 until n + 1) {
            if (j != b) {
                A[a][j] = B[a][j] / p
            }
        }
        for (i in 0 until m + 1) {
            for (j in 0 until n + 1) {
                if (i != a && j != b) {
                    A[i][j] = (B[a][b] * B[i][j] - B[a][j] * B[i][b]) / p
                }
            }
        }
    }

    private fun Primal(im: Int): Double {
        var J = n
        var I = 0
        var min = 0.0
        var PI = 0.0
        for (j in 0 until n) {
            if (A[m][j] < min) {
                min = A[m][j]
                J = j
            }
        }
        var sum = -1.0
        var ratio = 0.0
        for (i in 0 until m) {
            if (A[i][J] > 0) {
                ratio = A[i][n] / A[i][J]
                if (sum == -1.0 || sum > ratio) {
                    sum = ratio
                    I = i
                }
            }
        }
        if (im == 1) {
            PI = A[I][n] * A[m][J] / A[I][J]
            if (PI < 0) {
                PI *= -1.0
            }
            return PI
        }
        Operate(I, J)
        val temp = M[I + n]
        M[I + n] = M[J]
        M[J] = temp
        return 0.0
    }

    private fun Dual(im: Int): Double {
        var I = m
        var J = 0
        var min = 0.0
        var DI = 0.0
        for (i in 0 until m) {
            if (A[i][n] < min) {
                min = A[i][n]
                I = i
            }
        }
        var sum = -1.0
        var ratio = 0.0
        for (j in 0 until n) {
            if (A[I][j] < 0) {
                ratio = A[m][j] / A[I][j]
                if (sum == -1.0 || sum > ratio) {
                    sum = ratio
                    J = j
                }
            }
        }
        if (im == 1) {
            DI = A[I][n] * A[m][J] / A[I][J]
            if (DI < 0) {
                DI *= -1.0
            }
            return DI
        }
        Operate(I, J)
        val temp = M[I + n]
        M[I + n] = M[J]
        M[J] = temp
        return 0.0
    }

    private fun simplex() {
        if (++count > threshold) throw Exception("No Solution Found! Please change the feeds selection.")
        Log.d("Simplex", "count: $count")
        var c1 = 0
        var c2 = 0
        for (i in 0 until m) {
            if (A[i][n] < 0) {
                c1 = 1
            }
        }
        for (i in 0 until n) {
            if (A[m][i] < 0) {
                c2 = 1
            }
        }
        if (c1 == 0 && c2 == 0) return
        else if (c1 == 0) Primal(0)
        else if (c2 == 0) Dual(0)
        else {
            val PI = Primal(1)
            val DI = Dual(1)
            if (PI >= DI) Primal(0) else Dual(0)
        }
        simplex()
    }

    private fun Result() {
        ans = arrayOfNulls(n)
        for (i in 0 until n) ans[i] = 0.0
        for (i in 0 until m) {
            if (M[i + n] < n) {
                ans[M[i + n]] = A[i][n]
            }
        }
    }

    private fun CreateEquations() {
        n = feeds.size
        m = n + 10
        nutrients = Nutrients.getInstance()

        M = IntArray(m + n)
        for (i in 0 until m + n) { M[i] = i }

        A = Array(m + 1) { Array(n + 1) { 0.0 } }
        for (i in 0 until m + 1) for (j in 0 until n + 1) A[i][j] = 0.0

        for ((i, f) in feeds.withIndex()) {
            A[0][i] = f.details[0]
            A[1][i] = -1 * f.details[0]
        }
        A[0][n] = nutrients.dm * 100
        A[1][n] = -1 * nutrients.dm * 100
        for ((i, f) in feeds.withIndex()) {
            A[2][i] = f.details[1]
            A[3][i] = -1 * f.details[1]
        }
        A[2][n] = nutrients.cp / 10 * 1.1
        A[3][n] = -1 * nutrients.cp / 10
        for ((i, f) in feeds.withIndex()) {
            println(f.details.toList())
            A[4][i] = f.details[2]
            A[5][i] = -1 * f.details[2]
        }
        A[4][n] = nutrients.tdn / 10 * 1.1
        A[5][n] = -1 * nutrients.tdn / 10
        for (i in 0 until n) {
            if (i < 8) {
                A[6][i] = 1.0
                A[7][i] = -1.0
                A[8][i] = 0.0
                A[9][i] = 0.0
            } else {
                A[6][i] = 0.0
                A[7][i] = 0.0
                A[8][i] = 1.0
                A[9][i] = -1.0
            }
        }
        A[6][n] = nutrients.dm * 0.8
        A[7][n] = -1 * nutrients.dm * 0.4
        A[8][n] = nutrients.dm * 0.7
        A[9][n] = -1 * nutrients.dm * 0.2

        for ((i, f) in feeds.withIndex()) {
            A[10 + i][i] = 1.0
            A[10 + i][n] = f.percentage[nutrients.type] * nutrients.dm / 100
        }
        for ((i, f) in feeds.withIndex()) {
            A[m][i] = f.cost
        }
    }

    companion object {
        private lateinit var feeds: List<Feed>
        var FeedValues: List<List<Double>> = mutableListOf()
        var percent: Array<Double> = emptyArray()
//        var FeedValues = arrayOf(
//            arrayOf(20.0, 8.0, 52.0, 0.38, 0.36, 40.0),
//            arrayOf(90.0, 7.0, 50.0, 0.3, 0.25, 10.0),
//            arrayOf(20.0, 8.0, 60.0, 0.53, 0.14, 40.0),
//            arrayOf(20.0, 15.8, 60.0, 1.44, 0.14, 10.0),
//            arrayOf(90.0, 3.5, 40.0, 0.18, 0.08, 20.0),
//            arrayOf(90.0, 6.0, 42.0, 0.15, 0.09, 10.0),
//            arrayOf(90.0, 3.3, 42.0, 0.3, 0.06, 10.0),
//            arrayOf(90.0, 3.0, 42.0, 0.53, 0.14, 40.0),
//            arrayOf(90.0, 8.1, 79.2, 0.53, 0.41, 10.0),
//            arrayOf(90.0, 42.0, 70.0, 0.36, 1.0, 10.0),
//            arrayOf(90.0, 22.0, 70.0, 0.2, 0.9, 10.0),
//            arrayOf(90.0, 32.0, 70.0, 0.31, 0.72, 10.0),
//            arrayOf(75.0, 12.0, 70.0, 1.067, 0.093, 10.0),
//            arrayOf(90.0, 17.0, 70.0, 0.28, 0.54, 10.0),
//            arrayOf(90.0, 16.0, 110.0, 0.3, 0.62, 10.0),
//            arrayOf(90.0, 18.0, 45.0, 0.3, 0.62, 10.0),
//            arrayOf(90.0, 22.0, 70.0, 0.5, 0.45, 10.0),
//            arrayOf(90.0, 20.0, 65.0, 0.5, 0.4, 10.0),
//            arrayOf(92.3, 50.0, 77.0, 0.29, 0.68, 10.0),
//            arrayOf(90.0, 0.0, 0.0, 32.0, 15.0, 0.1),
//            arrayOf(90.0, 0.0, 0.0, 0.0, 0.0, 0.1)
//        )
//        var percent = arrayOf(40.0,10.0,40.0,10.0,20.0,10.0,10.0,40.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,0.1,0.1)
//        var cost = doubleArrayOf(
//            3.0, 3.0, 3.0, 2.0, 3.0, 3.5, 2.0, 3.0, 17.0, 38.0, 23.0, 23.0, 17.0, 14.0, 21.0, 20.0,
//            17.0, 15.0, 15.0, 60.0, 5.0
//        )
    }
}