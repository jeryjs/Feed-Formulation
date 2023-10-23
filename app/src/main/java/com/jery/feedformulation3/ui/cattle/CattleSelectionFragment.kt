package com.jery.feedformulation3.ui.cattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jery.feedformulation3.R

data class Cattle(val name: String, val imageResId: Int, val options: List<String>)

class CattleSelectionFragment : Fragment() {

    private val cattleList = listOf(
        Cattle("Crossbred Diary Cow", R.drawable.cattle_cow, listOf("BW", "MY", "MF", "Preg")),
        Cattle("Milch Buffalo", R.drawable.cattle_buffalo, listOf("BW", "MY", "MF", "Preg"))
    )

    private var selectedCattle: Cattle? = null

    private lateinit var selectionText: TextView
    private lateinit var cattleItemsContainer: LinearLayout
    private lateinit var cattleImage: ImageView
    private lateinit var optionsLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cattle_selection, container, false)

        selectionText = view.findViewById(R.id.selection_text)
        cattleItemsContainer = view.findViewById(R.id.cattle_items_container)
        cattleImage = view.findViewById(R.id.cattle_image)
        optionsLabel = view.findViewById(R.id.options_label)

        setupCattleItems()
        return view
    }

    private fun setupCattleItems() {
        cattleItemsContainer.removeAllViews()

        val rows = cattleList.chunked(2)
        rows.forEach { rowCattles ->
            val rowLayout = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                weightSum = 2f
            }

            rowCattles.forEach { cattle ->
                val cattleItemView = layoutInflater.inflate(
                    R.layout.item_cattle,
                    rowLayout,
                    false
                ) as TextView

                cattleItemView.apply {
                    text = cattle.name
                    setOnClickListener {
                        onCattleSelected(cattle)
                    }
                }

                rowLayout.addView(cattleItemView)
            }

            cattleItemsContainer.addView(rowLayout)
        }
    }

    private fun onCattleSelected(cattle: Cattle) {
        selectedCattle = cattle
        selectionText.text = "Selected Cattle: ${cattle.name}"
        showCattleDetails()
    }

    private fun showCattleDetails() {
        selectedCattle?.let { cattle ->
            cattleImage.setImageResource(cattle.imageResId)

            optionsLabel.text = "Select Options:"

            // Add the OptionDropdown views here

            cattleItemsContainer.visibility = View.GONE
            cattleImage.visibility = View.VISIBLE
            optionsLabel.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(): CattleSelectionFragment = CattleSelectionFragment()
    }
}
