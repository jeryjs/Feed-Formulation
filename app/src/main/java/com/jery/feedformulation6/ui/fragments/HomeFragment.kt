package com.jery.feedformulation6.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jery.feedformulation6.*
import com.jery.feedformulation6.ui.activities.FeedsSelection
import org.apache.commons.math3.optim.PointValuePair
import org.apache.commons.math3.optim.linear.LinearConstraint
import org.apache.commons.math3.optim.linear.LinearConstraintSet
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction
import org.apache.commons.math3.optim.linear.Relationship
import org.apache.commons.math3.optim.linear.SimplexSolver
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val log = view.findViewById<TextView>(R.id.log)
        val btn = view.findViewById<Button>(R.id.start_button)
        btn.setOnClickListener {
            val intent = Intent(activity, FeedsSelection::class.java).setAction("selectFeeds")
            startActivity(intent)


            val dataDict = mapOf(
                "obj" to listOf(3.0, 3.0, 3.0, 3.0, 3.5, 3.0, 17.0, 38.0, 23.0, 23.0, 17.0, 21.0, 20.0, 17.0, 15.0, 15.0, 60.0, 5.0),
                "cp" to listOf(8.0, 7.0, 8.0, 3.5, 6.0, 3.0, 8.1, 42.0, 22.0, 32.0, 12.0, 16.0, 18.0, 22.0, 20.0, 50.0, 0.0, 0.0),
                "tdn" to listOf(52.0, 50.0, 60.0, 40.0, 42.0, 42.0, 79.2, 70.0, 70.0, 70.0, 70.0, 72.2, 45.0, 70.0, 65.0, 77.0, 0.0, 0.0),
                "ca" to listOf(0.38, 0.3, 0.53, 0.18, 0.15, 0.53, 0.53, 0.36, 0.2, 0.31, 1.067, 0.3, 0.3, 0.5, 0.5, 0.29, 32.0, 0.0),
                "ph" to listOf(0.36, 0.25, 0.14, 0.08, 0.09, 0.14, 0.41, 1.0, 0.9, 0.72, 0.093, 0.62, 0.62, 0.45, 0.4, 0.68, 15.0, 0.0),
                "per" to listOf(40.0, 10.0, 40.0, 20.0, 10.0, 40.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 0.1, 0.1)
            )
            log.text = optimization(dataDict, log)
        }
//        btn.performClick()
        return view
    }

    private fun optimization(data: Map<String, List<Double>>, log:View):String {
        var string = ""

//        val data = mapOf(
//            "obj" to listOf(3.0, 3.0, 3.0, 3.0, 0.0),
//            "cp" to listOf(8.0, 7.0, 8.0, 3.5, 0.0),
//            "tdn" to listOf(52.0, 50.0, 60.0, 40.0, 0.0),
//            "ca" to listOf(0.38, 0.3, 0.53, 0.18, 0.0),
//            "ph" to listOf(0.36, 0.25, 0.14, 0.08, 0.0),
//            "per" to listOf(40.0, 10.0, 40.0, 20.0, 0.0),
//        )
//
//            val obj = data["obj"]!!.toDoubleArray()
//            val cp = data["cp"]!!.toDoubleArray()
//            val tdn = data["tdn"]!!.toDoubleArray()
//            val ca = data["ca"]!!.toDoubleArray()
//            val ph = data["ph"]!!.toDoubleArray()
//            val per = data["per"]!!.toDoubleArray()
//
//            val numVariables = obj.size
//
//            //describe the optimization problem
//            val f = LinearObjectiveFunction(doubleArrayOf(0.0, 0.0), 5.0)
//            val constraints: MutableCollection<LinearConstraint> = ArrayList()
////            constraints.add(LinearConstraint(doubleArrayOf(2.0, 8.0), Relationship.LEQ, 13.0))
////            constraints.add(LinearConstraint(doubleArrayOf(5.0, -1.0), Relationship.LEQ, 11.0))
////            constraints.add(LinearConstraint(doubleArrayOf(1.0, 0.0), Relationship.GEQ, 0.0))
////            constraints.add(LinearConstraint(doubleArrayOf(0.0, 1.0), Relationship.GEQ, 0.0))
//            constraints.add(LinearConstraint(DoubleArray(numVariables) { 1.0 }, Relationship.EQ, 1.06))
//            constraints.add(LinearConstraint(cp.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.176))
//            constraints.add(LinearConstraint(tdn.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.688))
//
//                //create and run solver
//            val solution: PointValuePair? = SimplexSolver().optimize(f, LinearConstraintSet(constraints), GoalType.MINIMIZE)
//            if (solution != null) {
//                //get solution
//                val min: Double = solution.value
//
//                string += "Opt: $min\n"
//                println("Opt: $min")
//
//                //print decision variables
//                for (i in 0..1) {
//                    string += solution.point[i]
//                    print(solution.point[i])
//                }
//            }

            // Assign variables
            val obj = data["obj"]!!.toDoubleArray()
            val cp = data["cp"]!!.toDoubleArray()
            val tdn = data["tdn"]!!.toDoubleArray()
            val ca = data["ca"]!!.toDoubleArray()
            val ph = data["ph"]!!.toDoubleArray()
            val per = data["per"]!!.toDoubleArray()

            // Print the variables
            println("\"obj\": $obj,")
            println("\"cp\": $cp,")
            println("\"tdn\": $tdn,")
            println("\"ca\": $ca,")
            println("\"ph\": $ph,")
            println("\"per\": $per")

            val solver = SimplexSolver()
            val f = LinearObjectiveFunction(doubleArrayOf(0.0, 5.0),0.0)
            val constraints: MutableCollection<LinearConstraint> = ArrayList()

            // Equations
    //                constraints.add(LinearConstraint(Array(obj.size) { 1.0 }.toDoubleArray(), Relationship.EQ, 1.06))
            constraints.add(LinearConstraint(cp.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.176))
    //                constraints.add(LinearConstraint(tdn.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.688))
    //                constraints.add(LinearConstraint(doubleArrayOf(0.3 * 1.06, 0.3 * 1.06, -0.4 * 1.06, -0.4 * 1.06), Relationship.EQ, 0.0))
    //                constraints.add(LinearConstraint(per.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.4 * 1.06))
    //                constraints.add(LinearConstraint(per.map { it / 100.0 }.toDoubleArray(), Relationship.LEQ, 0.6 * 1.11))
    //                constraints.add(LinearConstraint(doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.01, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.02), Relationship.GEQ, 0.01 * 1.06))
    //                constraints.add(LinearConstraint(ca.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.00424))
    //                constraints.add(LinearConstraint(ca.map { it / 100.0 }.toDoubleArray(), Relationship.LEQ, 0.00428))
    //                constraints.add(LinearConstraint(ph.map { it / 100.0 }.toDoubleArray(), Relationship.GEQ, 0.004134))
    //                constraints.add(LinearConstraint(ph.map { it / 100.0 }.toDoubleArray(), Relationship.LEQ, 0.00418))

            return try {
                val solution: PointValuePair? = solver.optimize(f, LinearConstraintSet(constraints), GoalType.MINIMIZE)
                val results = solution!!.point.toList()

                println("Solution: $solution")
                println("Results: $results")
                println("Constraints: $constraints")
                println("Objective: $f")
                println("Point: ${solution.point}")
                println("Value: ${solution.value}")
                println("first: ${solution.first}")

                string += "Solution: $solution\n"
                string += "Results: $results\n"
                string += "Constraints: $constraints\n"
                string += "Objective: $f\n"
                string += "Point: ${solution.point}\n"
                string += "Value: ${solution.value}\n"
                string += "first: ${solution.first}\n"

                //get solution
                val min: Double = solution.value

                string += "Opt: $min\n"
                println("Opt: $min")

                //print decision variables
                for (i in 0..1) {
                    string += solution.point[i]
                    print(solution.point[i])
                }
                var result = ""
                for (i in results.indices) {
                    result += "x${i + 1}: ${results[i]}\n"
                    string += result
                    println("x${i + 1}: ${results[i]}")
                }
//                results
                string
            } catch (e: Exception) {
                println(e)
                string += "$e"
//                listOf(0.0,0.0,0.0,0.0)
                string
            }

    }
}