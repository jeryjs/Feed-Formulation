package com.jery.feedformulation.ui.feedslist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jery.feedformulation.R
import com.jery.feedformulation.databinding.DialogAddNewFeedBinding
import com.jery.feedformulation.databinding.FragmentFeedsBinding
import com.jery.feedformulation.model.Feed
import com.jery.feedformulation.ui.adapter.FeedAdapter
import com.jery.feedformulation.utils.Utils
import com.jery.feedformulation.viewmodel.FeedsViewModel

class FeedsFragment : Fragment() {

    private var _binding: FragmentFeedsBinding? = null
    private val binding get() = _binding!!
    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedsViewModel = ViewModelProvider(this)[FeedsViewModel::class.java]

        setupRecyclerView()
        setupFabOptions()
    }

    private fun setupRecyclerView() {
        val feedAdapter = FeedAdapter(
            feedsViewModel.feeds,
            viewLifecycleOwner,
            isSelectFeedsEnabled = false,
            onFeedSelected = { feed ->
                Toast.makeText(requireContext(), "Feed: ${feed.name}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = feedAdapter
    }

    private fun setupFabOptions() {
        binding.addNewFeedsFab.setOnClickListener { showAddNewFeedDialog() }
        binding.exportFeedsFab.setOnClickListener { exportFeeds() }
        binding.importFeedsFab.setOnClickListener { importFeeds() }
        binding.resetFeedsFab.setOnClickListener { resetFeeds() }
    }

    private fun showAddNewFeedDialog() {
        val dialogBinding = DialogAddNewFeedBinding.inflate(requireActivity().layoutInflater)
        val typeArray = resources.getStringArray(R.array.type_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeArray)
        dialogBinding.sprType.setAdapter(adapter)
        dialogBinding.sprType.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) Utils.hideKeyboard(view)
            dialogBinding.sprType.showDropDown()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("Add New Feed #${binding.recyclerView.adapter?.itemCount?.plus(1)}")
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Help") { _, _ -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/gview?url=https://www.nddb.coop/sites/default/files/pdfs/Animal-Nutrition-booklet.pdf"))) }
            .setCancelable(false)
            .show()

        val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton.isEnabled = false

        val requiredFields = listOf(dialogBinding.edtName, dialogBinding.edtCost, dialogBinding.sprType, dialogBinding.edtDM, dialogBinding.edtCP, dialogBinding.edtTDN)
        requiredFields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    saveButton.isEnabled = requiredFields.all { it.text.isNotEmpty() }
                }
            })
        }

        saveButton.setOnClickListener {
            val newFeed = Feed(
                name = dialogBinding.edtName.text.toString().trim(),
                cost = dialogBinding.edtCost.text.toString().toDouble(),
                type = dialogBinding.sprType.text.toString().substring(0, 1),
                details = listOf(dialogBinding.edtDM, dialogBinding.edtCP, dialogBinding.edtTDN).map { it.text.toString().toDouble() },
                percentage = listOf(dialogBinding.edtMinIncl1, dialogBinding.edtMinIncl2).map { it.text.toString().toDouble() },
                checked = dialogBinding.cbChecked.isChecked,
                id = binding.recyclerView.adapter?.itemCount?.plus(1) ?: 1
            )
            feedsViewModel.addNewFeed(newFeed)
            dialog.dismiss()
        }
    }

    private fun exportFeeds() {
        val timeStamp = Utils.generateTimestamp()
        val fileName = "feeds_export_$timeStamp.json"

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        startActivityForResult(intent, EXPORT_FEEDS_REQUEST_CODE)
    }

    private fun importFeeds() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        startActivityForResult(intent, IMPORT_FEEDS_REQUEST_CODE)
    }

    private fun resetFeeds() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reset Feeds List")
            .setMessage("Are you sure you want to reset the feeds list? This action cannot be undone.")
            .setPositiveButton("Reset") { _, _ ->
                feedsViewModel.resetFeeds()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EXPORT_FEEDS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { feedsViewModel.exportFeeds(it) }
                }
            }
            IMPORT_FEEDS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { feedsViewModel.importFeeds(it) }
                }
            }
        }
    }

    companion object {
        private const val EXPORT_FEEDS_REQUEST_CODE = 1001
        private const val IMPORT_FEEDS_REQUEST_CODE = 1002
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}