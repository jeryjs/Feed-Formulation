@file:Suppress("KDocUnresolvedReference", "PropertyName", "PrivatePropertyName", "LocalVariableName")

package com.jery.feedformulation.utils

import android.util.Log
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.data.Nutrients

class Simplex internal constructor() {
    private lateinit var nutrients: Nutrients
    private lateinit var A: Array<Array<Double>>
    private lateinit var M: IntArray
    private var m = 0
    private var n = 0
    lateinit var ans: Array<Double>
    var total_dm = 0.0
    var total_cp = 0.0
    var total_tdn = 0.0
    private var count = 0
    private var threshold = 2000

    /**
     * This function solves a linear programming problem using the simplex method.
     * It takes a list of Feed objects as input and uses them to create equations, solve them using the simplex method,
     * and print the results. It also calculates and prints the total dry matter, crude protein, and total digestible nutrients
     * for the feeds used in the problem.
     *
     * @param feedsList The list of Feed objects to use in the problem.
     *
     * The function uses the following variables:
     * - [feeds]: A list of Feed objects used in the problem.
     * - [n]: The number of feeds used in the problem.
     * - [ans]: An array of doubles representing the solution to the problem.
     * - [total_dm]: The total dry matter for the feeds used in the problem.
     * - [total_cp]: The total crude protein for the feeds used in the problem.
     * - [total_tdn]: The total digestible nutrients for the feeds used in the problem.
     * - [y]: A boolean variable used to track if the current feed is the one with the highest dry matter percentage.
     * - [D]: A double variable used to store the highest dry matter percentage.
     * - [P]: A boolean variable used to track if the current feed is the one with the highest protein percentage.
     * - [k]: A double variable used to store the highest protein percentage.
     */
    fun solve(feedsList: List<Feed>) {
        feeds = feedsList

        createEquations()
        simplex()
        result()

        var y = false; var D = 0.0; var P = false; var k = 0.0
        for (i in 0 until n) {
            total_cp += (feeds[i].details[1] * ans[i])
            total_tdn += (feeds[i].details[2] * ans[i])
            println("${feeds[i].details[0]}, $i, ${feeds[i].percentage}, ${ans[i]}")
            ans[i] = (100 * ans[i]) / feeds[i].details[0]

            if (i == 16) {
                y = true
                D = ans[i]
            }
            if (i == 17) {
                P = true
                k = ans[i]
            }
            if (i == 19) {
                if (y) ans[i] = ans[i] - 0.01 * D
                if (P) ans[i] = ans[i] - 0.01 * k
            }

            total_dm += (feeds[i].details[0] * ans[i])
        }

        total_dm /= 100
        total_cp *= 10
        total_tdn *= 10
    }

    /**
     * Creates the equations for the simplex algorithm.
     * This function takes in the necessary inputs from global vars and generates the
     * equations required for the simplex algorithm to solve the linear programming problem.
     * The equations are stored in a matrix format for further processing.
     * - [n]: the number of variables (feeds selected)
     * - [m]: the number of constraints applied to the linear programmnig problem
     * - [M]: an integer array used to keep track of the indices of the basic and non-basic variables in the simplex algorithm.
     * - [A]: a matrix representing the coefficients of the constraints
     */
    private fun createEquations() {
        n = feeds.size
        m = 15

        nutrients = Nutrients.getInstance()
        println("nutrients: ${nutrients.dm}, ${nutrients.cp}, ${nutrients.tdn}")

        M = IntArray(m + n)
        for (i in 0 until m + n) {
            M[i] = i
        }

        A = Array(m + 1) { Array(n + 1) { 0.0 } }

        for (i in 0 until n) {
            A[0][i] = 1.0
            A[1][i] = -1.0
        }
        A[0][n] = 1.1 * nutrients.dm
        A[1][n] = (-1 * nutrients.dm) * 0.85

        for (i in 0 until n) {
            A[2][i] = feeds[i].details[1]
            A[3][i] = -1 * feeds[i].details[1]
        }
        A[2][n] = nutrients.cp / 10 * 1.1
        A[3][n] = ((-1 * nutrients.cp) / 10) * 0.9

        for (i in 0 until n) {
            A[4][i] = feeds[i].details[2]
            A[5][i] = -1 * feeds[i].details[2]
        }
        A[4][n] = nutrients.tdn / 10 * 1.1
        A[5][n] = ((-1 * nutrients.tdn) / 10) * 0.95

        for (i in 0 until n) {
            if (i in 0 until 4) {
                A[6][i] = 0.3
                A[7][i] = -0.3
            }
            if (i in 4 until 8) {
                A[6][i] = -0.4
                A[7][i] = 0.4
            }
            else {
                A[6][i] = 0.0
                A[7][i] = 0.0
            }
        }
        A[6][n] = 0.0
        A[7][n] = 0.0

        for (i in 0 until n) {
            if (i in 8 until 19) {
                A[8][i] = 1.0
                A[9][i] = -1.0
            } else {
                A[8][i] = 0.0
                A[9][i] = 0.0
            }
        }
        A[8][n] = 0.6 * nutrients.dm
        A[9][n] = -0.4 * nutrients.dm

        for (i in 0 until n) {
            if (i == 19) {
                A[10][i] = 1.0
                A[11][i] = -1.0
            } else {
                A[10][i] = 0.0
                A[11][i] = 0.0
            }
        }
        A[10][n] = 0.0125 * nutrients.dm
        A[11][n] = -0.01 * nutrients.dm

        for (i in 0 until n) {
            if (i == 20) {
                A[12][i] = 1.0
                A[13][i] = -1.0
            } else {
                A[12][i] = 0.0
                A[13][i] = 0.0
            }
        }
        A[12][n] = 0.0075 * nutrients.dm;
        A[13][n] = -0.005 * nutrients.dm;

        for (i in 0 until n) {
            A[14][i] = if (i == 3) 1.0 else 0.0
        }
        A[14][n] = nutrients.dm / 100

        for (i in 0 until n) {
            A[15][i] = feeds[i].cost
        }
    }

    /**
     * This function implements the simplex algorithm to solve linear programming problems.
     * It iteratively solves the problem by either using the primal or dual method, depending on the problem's constraints.
     * The function stops when the optimal solution is found or when it is determined that the problem is unbounded.
     * @throws Exception if the number of iterations exceeds the threshold.
     *
     * - [count] and threshold: Counts the number of Simplex algorithm iterations and throws Exception if it exceeds the threshold.
     * - [c1] and c2: Check for negative values in the last column and row of matrix A, respectively.
     * - [PI] and DI: Indicators calculated to decide the next step in the Simplex method.
     * - [A]: A matrix holding coefficients of the constraints and objective function.
     * - [m] and n: Represent the number of constraints and variables in the problem, respectively.
     */
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

        if (c1 == 0 && c2 == 0)
            return
        else if (c1 == 0)
            primal(0)
        else if (c2 == 0)
            dual(0)
        else {
            val PI = primal(1)  // Primal Indicator
            val DI = dual(1)    // Dual Indicatior
            if (PI >= DI)
                primal(0)
            else
                dual(0)
        }
        simplex()
    }

    /**
     * This function implements the primal simplex algorithm to solve linear programming problems.
     * It takes an integer parameter 'im' which is used to determine whether to return the primal objective value or not.
     * The function operates on the matrix A and the vector M, which are defined outside the function.
     *
     * @param im an integer parameter used to determine whether to return the primal objective value or not.
     * @return the primal objective value if im is 1, otherwise returns 0.0
     *
     * Variables used:
     * - [J]: an integer variable used to store the column index of the pivot element.
     * - [I]: an integer variable used to store the row index of the pivot element.
     * - [min]: a double variable used to store the minimum value in the last row of the matrix A.
     * - [PI]: a double variable used to store the primal objective value.
     * - [sum]: a double variable used to store the minimum ratio of the last column to the pivot column.
     * - [ratio]: a double variable used to store the ratio of the last column to the pivot column for each row.
     */
    private fun primal(im: Int): Double {
        var J = n
        var I = 0
        var min = 0.0
        var PI: Double

        for (j in 0 until n) {
            if (A[m][j] < min) {
                min = A[m][j]
                J = j
            }
        }

        var sum = -1.0
        var ratio: Double
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
                PI *= -1
            }
            return PI
        }
        operate(I, J)
        val temp = M[I + n]
        M[I + n] = M[J]
        M[J] = temp
        return 0.0
    }

    /**
     * This function implements the dual simplex algorithm to solve linear programming problems.
     * It takes an integer parameter 'im' which is used to determine whether to return the dual objective value or not.
     * The function operates on the matrix A and the vector M, which are defined outside the function.
     *
     * @param im The iteration number.
     * @return The dual objective function value.
     *
     * Variables used:
     * - [J]: an integer variable used to store the column index of the pivot element.
     * - [I]: an integer variable used to store the row index of the pivot element.
     * - [min]: a double variable used to store the minimum value in the last row of the matrix A.
     * - [PI]: a double variable used to store the primal objective value.
     * - [sum]: a double variable used to store the minimum ratio of the last column to the pivot column.
     * - [ratio]: a double variable used to store the ratio of the last column to the pivot column for each row.
     */
    private fun dual(im: Int): Double {
        var I = m
        var J = 0
        var min = 0.0
        var DI: Double

        for (i in 0 until m) {
            if (A[i][n] < min) {
                min = A[i][n]
                I = i
            }
        }

        var sum = -1.0
        var ratio: Double
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
                DI *= -1
            }
            return DI
        }
        operate(I, J)

        val temp = M[I + n]
        M[I + n] = M[J]
        M[J] = temp
        return 0.0
    }

    /**
     * This function performs an operation on a given matrix A, where it takes two indices a and b, and modifies the matrix A in place.
     * It calculates the inverse of the element at index (a, b) and updates the corresponding row and column of the matrix A.
     * It also updates the remaining elements of the matrix A using the formula ((B[a][b] * B[i][j]) - (B[a][j] * B[i][b])) / p.
     * - [a]: The row index of the element to be operated on.
     * - [b]: The column index of the element to be operated on.
     * - [A]: The matrix to be operated on.
     * - [m]: The number of rows in the matrix A.
     * - [n]: The number of columns in the matrix A.
     */
    private fun operate(a: Int, b: Int) {
        val p: Double

        val B = A.map { it.clone() }

        p = B[a][b]
        A[a][b] = 1 / p

        for (i in 0 until m+1) {
            if (i != a) {
                A[i][b] = -1 * B[i][b] / p
            }
        }

        for (j in 0 until n+1) {
            if (j != b) {
                A[a][j] = B[a][j] / p
            }
        }
        for (i in 0 until m+1) {
            for (j in 0 until n+1) {
                if ((i != a) && (j != b)) {
                    A[i][j] = ((B[a][b] * B[i][j]) - (B[a][j] * B[i][b])) / p
                }
            }
        }
    }

    /**
     * This function performs the simplex algorithm to solve a linear programming problem in standard form.
     * It takes in the problem's constraint matrix, objective function coefficients, and right-hand side values,
     * and returns the optimal solution and optimal objective value, if they exist.
     * - [ans]: an array of doubles that will store the optimal solution
     * - [n]: the number of variables (feeds selected)
     * - [M]: an array used to keep track of the indices of the basic and non-basic variables in the simplex algorithm.
     * - [A]: a matrix representing the coefficients of the constraints
     */
    private fun result() {
        ans = Array(n) { 0.0 }
        for (i in 0 until m) {
            if (M[i + n] < n) {
                ans[M[i + n]] = A[i][n]
            }
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