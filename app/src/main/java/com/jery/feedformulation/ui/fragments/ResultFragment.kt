package com.jery.feedformulation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jery.feedformulation.data.Feed
import com.jery.feedformulation.data.Nutrients
import com.jery.feedformulation.databinding.FragmentResultBinding
import com.jery.feedformulation.utils.Simplex

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment(private val selectedFeeds: List<Feed>, private val selectedFeedsIndex: List<Int>) : Fragment() {
    private lateinit var _b: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentResultBinding.inflate(inflater, container, false)

        val s = Simplex()
        s.solve(selectedFeeds, selectedFeedsIndex)

        var string = "SELECTED FEEDS [${selectedFeeds.size}]:\n\n"
        for ((i, feed) in selectedFeeds.withIndex() ) {
            Log.d("FeedSelectionActivity", "Selected Feed: ${feed.name}\t[${feed.details}]")
            string += "x$i:\t\t${feed.name}\t\t${Simplex.FeedValues[feed.id].toList()}\n"
        }
        _b.tvLog.text = string

        val nut = Nutrients.getInstance()
        string += "\n\nDATA:\n\n"
        string += "DM: ${nut.dm} | CP: ${nut.cp} | TDN: ${nut.tdn}\n"
        string += "Total DM:\t\t${"%.2f".format(s.total_dm)}\n"
        string += "Total CP:\t\t${"%.2f".format(s.total_cp)}\n"
        string += "Total TDN:\t\t${"%.2f".format(s.total_tdn)}\n"

        val weights = mutableListOf<Double>()
        string += "\n\nRESULT: [${s.ans.size}]\n\n"
        for ((i, feed) in selectedFeeds.withIndex()) {
            val weight = (s.ans[i]!! * 100.0) / 100.0
            weights.add(weight)
            if (weight > 0.0)
                string += "${feed.name}:\t${"%.2f".format(weight)} Kg\n"
            else
                string += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t${feed.name}:\t0 Kg\n"
        }
        string += "\nTotal Quantity to feed (weight) = ${"%.2f".format(weights.sum())} Kg"
        _b.tvLog.text = string

        return _b.root
    }
}