package ru.savenkov.paychecksapp.presentation.screens.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.databinding.FragmentSavedBinding
import ru.savenkov.paychecksapp.presentation.screens.check.CheckFragment

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavedViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                SavedViewModel((requireActivity().applicationContext as App).repository)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        val savedChecksAdapter = SavedChecksAdapter{checkId ->
            findNavController().navigate(
                R.id.action_navigation_saved_to_checkFragment,
                bundleOf(CheckFragment.CHECK_ID_KEY to checkId)
            )
        }

        val categoryAdapter = CategoryAdapter {category ->
            viewModel.sortSavedSortParams.category = category
            viewModel.getCheckListBySortParams()
            binding.selectedCategory.isVisible = true
            binding.selectedCategory.text = category
        }

        binding.savedHolder.adapter = savedChecksAdapter
        binding.categoriesHolder.adapter = categoryAdapter
        binding.selectedCategory.setOnCloseIconClickListener {
            viewModel.sortSavedSortParams.category = null
            viewModel.getCheckListBySortParams()
            it.isVisible = false
        }

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) return true
                viewModel.getCheckListBySearch(query)
                return false
            }
        })

        setFiltersFromSorting()

        viewModel.checksList.observe(viewLifecycleOwner) {
            binding.savedListEmptyLabel.isVisible = it.isNullOrEmpty()
            savedChecksAdapter.checkList = it
        }

        viewModel.categoryList.observe(viewLifecycleOwner) {
            categoryAdapter.categoryList = it as ArrayList<String>
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCheckListBySortParams()
    }

    private fun setFiltersFromSorting() {
        binding.sortPeriod.setOnClickListener {
            setDateRangePicker()
        }

        binding.sortPeriod.setOnCloseIconClickListener {
            binding.sortPeriod.text = getString(R.string.saved_sort_period)
            binding.sortPeriod.isCloseIconVisible = false
            viewModel.sortSavedSortParams.periodFrom = "0"
            viewModel.sortSavedSortParams.periodTill = "9"
            viewModel.getCheckListBySortParams()
        }

        setFragmentResultListener(SortSumDialogFragment.SORT_TOTAL_SUM_REQUEST_KEY) { _, bundle ->
            val fromRubles = bundle.getString(SortSumDialogFragment.SORT_TOTAL_SUM_FROM_KEY)
            val tillRubles = bundle.getString(SortSumDialogFragment.SORT_TOTAL_SUM_TILL_KEY)
            val totalSumLabel = "$fromRubles - $tillRubles"
            binding.sortTotalSum.text = totalSumLabel
            binding.sortTotalSum.isCloseIconVisible = true
            viewModel.sortSavedSortParams.sumFrom = (fromRubles!!.toLong() * 100).toString()
            viewModel.sortSavedSortParams.sumTill = (tillRubles!!.toLong() * 100).toString()
            viewModel.getCheckListBySortParams()
        }

        binding.sortTotalSum.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_saved_to_sortSumDialogFragment)
        }

        binding.sortTotalSum.setOnCloseIconClickListener {
            binding.sortTotalSum.text = getString(R.string.saved_sort_total_sum)
            binding.sortTotalSum.isCloseIconVisible = false
            viewModel.sortSavedSortParams.sumFrom = "0"
            viewModel.sortSavedSortParams.sumTill = "10000000"
            viewModel.getCheckListBySortParams()
        }
    }

    private fun setDateRangePicker() {
        val datePickerDialog = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.ThemeMaterialCalendar)
            .setTitleText("Выберите период для поиска")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        datePickerDialog.show(childFragmentManager, "DatePicker")

        datePickerDialog.addOnPositiveButtonClickListener {
            val startDate = Converter.convertTimeToDate(it.first)
            val endDate = Converter.convertTimeToDate(it.second)
            binding.sortPeriod.text = String.format("от $startDate до $endDate")
            binding.sortPeriod.isCloseIconVisible = true
            viewModel.sortSavedSortParams.periodFrom = startDate
            viewModel.sortSavedSortParams.periodTill = endDate
            viewModel.getCheckListBySortParams()
        }

        datePickerDialog.addOnNegativeButtonClickListener {
            datePickerDialog.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}