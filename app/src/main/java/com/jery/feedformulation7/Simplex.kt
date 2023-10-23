package com.jery.feedformulation7

import android.util.Log


class Simplex internal constructor() {
    var nutirents: Nutrients? = Nutrients.getInstance()
    var feedData: FeedData? = FeedData.getInstance()
    lateinit var A: Array<Array<Double>>
    lateinit var M: IntArray
    var m = 0
    var n = 0
    lateinit var ans: Array<Double?>
    var total_dm = 0.0
    var total_cp = 0.0
    var total_tdn = 0.0

    fun Operate(a: Int, b: Int) {
        try {
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
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun Primal(im: Int): Double {
        try {
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
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun Dual(im: Int): Double {
        try {
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
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun simplex() {
        try {
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
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun Result() {
        try {
            ans = arrayOfNulls(n)
            for (i in 0 until n) ans[i] = 0.0
            for (i in 0 until m) {
                if (M[i + n] < n) {
                    ans[M[i + n]] = A[i][n]
                }
            }
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun CreateEquations(feeds: ArrayList<Int>, values: ArrayList<Double>) {
        try {
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
                A[0][i] = FeedValues[feeds[i]][0]
                A[1][i] = -1 * FeedValues[feeds[i]][0]
            }
            A[0][n] = nutirents!!.DM * 100
            A[1][n] = -1 * nutirents!!.DM * 100
            for (i in 0 until n) {
                A[2][i] = FeedValues[feeds[i]][1]
                A[3][i] = -1 * FeedValues[feeds[i]][1]
            }
            A[2][n] = nutirents!!.CP / 10 * 1.1
            A[3][n] = -1 * nutirents!!.CP / 10
            for (i in 0 until n) {
                A[4][i] = FeedValues[feeds[i]][2]
                A[5][i] = -1 * FeedValues[feeds[i]][2]
            }
            A[4][n] = nutirents!!.TDN / 10 * 1.1
            A[5][n] = -1 * nutirents!!.TDN / 10
            for (i in 0 until n) {
                if (feeds[i] < 8) {
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
            A[6][n] = nutirents!!.DM * 0.8
            A[7][n] = -1 * nutirents!!.DM * 0.4
            A[8][n] = nutirents!!.DM * 0.7
            A[9][n] = -1 * nutirents!!.DM * 0.2
            for (i in 0 until n) {
                A[10 + i][i] = 1.0
                A[10 + i][n] = percent[feeds[i]] * nutirents!!.DM / 100
            }
            for (i in 0 until n) {
                A[m][i] = values[i]
            }
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    fun Solver() {
        try {
            CreateEquations(feedData!!.feedsSelected, feedData!!.feedCost)
            Log.v("Nutrients", "${nutirents!!.DM} -- ${nutirents!!.CP} -- ${nutirents!!.TDN}")

            simplex()
            Result()

            for (i in 0 until n) {
                total_dm += FeedValues[feedData!!.feedsSelected[i]][0] * ans[i]!!
                total_cp += FeedValues[feedData!!.feedsSelected[i]][1] * ans[i]!!
                total_tdn += FeedValues[feedData!!.feedsSelected[i]][2] * ans[i]!!
            }
        } catch (e: Exception) {
            throw Exception("Error finding solution.")
        }
    }

    companion object {
        var FeedValues = arrayOf(
            arrayOf(90.0, 3.5, 40.0, 0.18, 0.08, 20.0),
            arrayOf(20.0, 8.0, 52.0, 0.38, 0.36, 40.0),
            arrayOf(20.0, 8.0, 60.0, 0.53, 0.14, 40.0),
            arrayOf(90.0, 7.0, 50.0, 0.3, 0.25, 10.0),
            arrayOf(90.0, 6.0, 42.0, 0.15, 0.09, 10.0),
            arrayOf(20.0, 15.8, 60.0, 1.44, 0.14, 10.0),
            arrayOf(90.0, 3.3, 42.0, 0.3, 0.06, 10.0),
            arrayOf(90.0, 3.0, 42.0, 0.53, 0.14, 40.0),
            arrayOf(90.0, 8.1, 79.2, 0.53, 0.41, 10.0),
            arrayOf(90.0, 42.0, 70.0, 0.36, 1.0, 10.0),
            arrayOf(90.0, 22.0, 70.0, 0.2, 0.9, 10.0),
            arrayOf(90.0, 32.0, 70.0, 0.31, 0.72, 10.0),
            arrayOf(75.0, 12.0, 70.0, 1.067, 0.093, 10.0),
            arrayOf(90.0, 17.0, 70.0, 0.28, 0.54, 10.0),
            arrayOf(90.0, 16.0, 110.0, 0.3, 0.62, 10.0),
            arrayOf(90.0, 18.0, 45.0, 0.3, 0.62, 10.0),
            arrayOf(90.0, 22.0, 70.0, 0.5, 0.45, 10.0),
            arrayOf(90.0, 20.0, 65.0, 0.5, 0.4, 10.0),
            arrayOf(97.0, 0.0, 0.0, 36.0, 0.0, 1.0),
            arrayOf(96.0, 0.0, 0.0, 36.0, 0.0, 1.0),
            arrayOf(90.0, 0.0, 0.0, 32.0, 15.0, 0.1),
            arrayOf(90.0, 0.0, 0.0, 24.0, 16.0, 1.0),
            arrayOf(90.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            arrayOf(90.0, 0.0, 0.0, 0.0, 0.0, 0.5),
            arrayOf(98.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            arrayOf(95.0, 287.5, 0.0, 0.0, 0.0, 0.2)
        )
        var percent = arrayOf(20.0,40.0,40.0,10.0,10.0,10.0,10.0,40.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,1.0,1.0,0.1,1.0,0.0,0.5,0.0,0.2)
    }
}