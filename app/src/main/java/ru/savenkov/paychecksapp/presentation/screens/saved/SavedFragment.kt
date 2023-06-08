package ru.savenkov.paychecksapp.presentation.screens.saved

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
            viewModel.getChecksWithCategory(category)
            binding.selectedCategory.isVisible = true
            binding.selectedCategory.text = category
        }

        binding.savedHolder.adapter = savedChecksAdapter
        binding.categoriesHolder.adapter = categoryAdapter
        binding.selectedCategory.setOnCloseIconClickListener {
            viewModel.getCheckList()
            it.isVisible = false
        }

        setSorting()

        viewModel.checksList.observe(viewLifecycleOwner) {
            savedChecksAdapter.checkList = it
        }

        viewModel.categoryList.observe(viewLifecycleOwner) {
            categoryAdapter.categoryList = it as ArrayList<String>
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCheckList()
    }

    private fun setSorting() {
        binding.sortDate.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menuInflater.inflate(R.menu.saved_sort_date_menu, this.menu)
                setOnMenuItemClickListener { item ->
                    binding.sortDate.text = item.title.toString()
                    when (item.itemId) {

                    }
                    true
                }
            }.show()
        }

        binding.sortPeriod.setOnClickListener {
            setDateRangePicker()
        }

        binding.sortTotalSum.setOnClickListener {
            val popUpMenu = PopupMenu(requireContext(), it)
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.saved_sum_slider)
            dialog.show()
        }

        binding.sortPeriod.setOnCloseIconClickListener {
            binding.sortPeriod.text = getString(R.string.saved_sort_period)
            binding.sortPeriod.isCloseIconVisible = false
            viewModel.getCheckList()
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
        datePickerDialog.show(childFragmentManager, "TAFAF")

        datePickerDialog.addOnPositiveButtonClickListener {
            val startDate = Converter.convertTimeToDate(it.first)
            val endDate = Converter.convertTimeToDate(it.second)
            binding.sortPeriod.text = String.format("от $startDate до $endDate")
            binding.sortPeriod.isCloseIconVisible = true
            viewModel.getCheckListByPeriod(startDate, endDate)
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