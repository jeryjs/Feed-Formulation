package com.jery.feedformulation.ui.tmrmaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.FragmentCattleSelectionBinding
import com.jery.feedformulation.databinding.ItemCattleBinding
import com.jery.feedformulation.viewmodel.TmrmakerViewModel

class CattleSelectionFragment : Fragment() {

    private var _binding: FragmentCattleSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var tmrmakerViewModel: TmrmakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmrmakerViewModel = ViewModelProvider(requireActivity())[TmrmakerViewModel::class.java]
        _binding = FragmentCattleSelectionBinding.inflate(inflater, container, false)

        try {
            (context as AppCompatActivity).supportActionBar!!.title = getString(R.string.select_cattle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val cattleArray = resources.getStringArray(R.array.cattle_array)
        val cattleIcons = listOf(R.drawable.ic_cattle_cow, R.drawable.ic_cattle_buffalo)

        val adapter = CattleAdapter(cattleArray, cattleIcons) { position ->
            tmrmakerViewModel.selectCattle(position)
            navigateToNextFragment()
        }

        binding.recyclerViewCattle.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCattle.adapter = adapter

        return binding.root
    }

    private fun navigateToNextFragment() {
        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.fragment_container_tmrmaker, NutrientSelectionFragment())
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class CattleAdapter(
        private val cattleArray: Array<String>,
        private val cattleIcons: List<Int>,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<CattleAdapter.CattleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CattleViewHolder {
            val binding: ItemCattleBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_cattle,
                parent,
                false
            )
            return CattleViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CattleViewHolder, position: Int) {
            holder.bind(cattleArray[position], cattleIcons[position % cattleIcons.size])
        }

        override fun getItemCount(): Int = cattleArray.size

        inner class CattleViewHolder(private val binding: ItemCattleBinding) :
            RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    onItemClick(adapterPosition)
                }
            }

            fun bind(cattleName: String, cattleIcon: Int) {
                binding.imageCattle.setImageResource(cattleIcon)
                binding.textCattleName.text = cattleName
                binding.textCattleDesc.text =
                    resources.getStringArray(R.array.cattle_desc_array)[adapterPosition]
            }
        }
    }
}