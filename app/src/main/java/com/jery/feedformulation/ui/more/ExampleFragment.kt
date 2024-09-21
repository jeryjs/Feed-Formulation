package com.jery.feedformulation.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentExampleBinding

class ExampleFragment : Fragment() {
    private var _binding: FragmentExampleBinding? = null
    private val binding get() = _binding!!

    private var exampleType: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExampleBinding.inflate(inflater, container, false)

        (context as AppCompatActivity).supportActionBar!!.title.toString().let {
            exampleType = when (it) {
                resources.getString(R.string.menu_example_tmr) -> 0
                resources.getString(R.string.menu_example_cm) -> 1
                else -> 0
            }
        }

        val imageIds = listOf(
                listOf(R.raw.ration_thumb_rule),
                listOf(
                    R.raw.concentrate_mixture_formula_type_1,
                    R.raw.concentrate_mixture_formula_type_2,
                    R.raw.concentrate_mixture_formula_type_3
                )
            )[exampleType]

        val adapter = ImageAdapter(imageIds)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ImageAdapter(private val imageIds: List<Int>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        inner class ImageViewHolder(val imageView: ImageView) :
            RecyclerView.ViewHolder(imageView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                adjustViewBounds = true
            }
            return ImageViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageId = imageIds[position]
            holder.imageView.setImageResource(imageId)
        }

        override fun getItemCount(): Int = imageIds.size
    }
}