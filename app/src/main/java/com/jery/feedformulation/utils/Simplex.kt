package com.jery.feedformulation.utils

import android.util.Log
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.data.Nutrients


class Simplex internal constructor() {
    var nutirents: Nutrients
    lateinit var A: Array<Array<Double>>
    lateinit var M: IntArray
    var m = 0
    var n = 0
    lateinit var ans: Array<Double?>
    var total_dm = 0.0
    var total_cp = 0.0
    var total_tdn = 0.0

    init {
        nutirents = Nutrients.getInstance()
    }

    fun Operate(a: Int, b: Int) {
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

    fun Primal(im: Int): Double {
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

    fun Dual(im: Int): Double {
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

    fun simplex() {
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

    fun Result() {
        ans = arrayOfNulls(n)
        for (i in 0 until n) ans[i] = 0.0
        for (i in 0 until m) {
            if (M[i + n] < n) {
                ans[M[i + n]] = A[i][n]
            }
        }
    }

    private fun CreateEquations(feeds: List<Feed>) {
        n = feeds.size
        m = n + 10
        M = IntArray(m + n)
        nutirents = Nutrients.getInstance()
        for (i in 0 until m + n) {
            M[i] = i
        }
        A = Array(m + 1) { Array(n + 1) { 0.0 } }
        for (i in 0 until m + 1) for (j in 0 until n + 1) A[i][j] = 0.0
        for (i in 0 until n) {
            A[0][i] = FeedValues[i][0]
            A[1][i] = -1 * FeedValues[i][0]
        }
        A[0][n] = nutirents.dm * 100
        A[1][n] = -1 * nutirents.dm * 100
        for (i in 0 until n) {
            A[2][i] = FeedValues[i][1]
            A[3][i] = -1 * FeedValues[i][1]
        }
        A[2][n] = nutirents.cp / 10 * 1.1
        A[3][n] = -1 * nutirents.cp / 10
        for (i in 0 until n) {
            A[4][i] = FeedValues[i][2]
            A[5][i] = -1 * FeedValues[i][2]
        }
        A[4][n] = nutirents.tdn / 10 * 1.1
        A[5][n] = -1 * nutirents.tdn / 10
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
        A[6][n] = nutirents.dm * 0.8
        A[7][n] = -1 * nutirents.dm * 0.4
        A[8][n] = nutirents.dm * 0.7
        A[9][n] = -1 * nutirents.dm * 0.2
        for (i in 0 until n) {
            A[10 + i][i] = 1.0
            A[10 + i][n] = percent[i] * nutirents.dm / 100
        }
        for (i in 0 until n) {
            A[m][i] = feeds[i].cost
        }
    }

    // Entry point into the simplex solver
    fun Solver(feeds: List<Feed>) {
        FeedValues = feeds.map { itt -> itt.details.map { it }.toTypedArray() }.toTypedArray()
        CreateEquations(feeds)
        Log.v("Nutrients", "${nutirents.dm} -- ${nutirents.cp} -- ${nutirents.tdn}")

        simplex()
        Result()

        for (i in 0 until n) {
            total_dm += FeedValues[i][0] * ans[i]!!
            total_cp += FeedValues[i][1] * ans[i]!!
            total_tdn += FeedValues[i][2] * ans[i]!!
        }
    }

    companion object {
        var FeedValues = arrayOf(
            arrayOf(90.0, 3.5, 40.0, 0.18, 0.08, 20.0),
            arrayOf(20.0, 8.0, 52.0, 0.38, 0.36, 40.0),
            arrayOf(20.0, 8.0, 60.0, 0.53, 0.14, 40.0),
        )
        var percent = arrayOf(20.0,40.0,40.0,10.0,10.0,10.0,10.0,40.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,1.0,1.0,0.1,1.0,0.0,0.5,0.0,0.2)
    }
}