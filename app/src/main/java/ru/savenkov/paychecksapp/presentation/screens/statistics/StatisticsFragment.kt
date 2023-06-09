package ru.savenkov.paychecksapp.presentation.screens.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.converter.Converter
import ru.savenkov.paychecksapp.databinding.FragmentStatisticsBinding
import ru.savenkov.paychecksapp.presentation.model.CheckGood


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticsViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                StatisticsViewModel((requireActivity().applicationContext as App).repository)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        if (savedInstanceState == null) viewModel.getStatisticsList()
        return binding.root
    }

    override fun onResume() {
        viewModel.sortPeriodDate = kotlin.Pair("0","9")
        viewModel.getStatisticsList()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatisticAdapter { sortMenuId ->
            when (sortMenuId) {
                R.id.sort_asc -> viewModel.goodsSortType = SortType.BY_ASC
                R.id.sort_desc -> viewModel.goodsSortType = SortType.BY_DESC
            }
            viewModel.getStatisticsList()
        }
        binding.statisticsHolder.adapter = adapter
        viewModel.statisticList.observe(viewLifecycleOwner) {
            binding.savedListEmptyLabel.isVisible = it.size == 1
            adapter.statisticList = it
        }

        binding.sortPeriod.setOnClickListener {
            setDateRangePicker()
        }
    }

    private fun setDateRangePicker() {
        val datePickerDialog = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.ThemeMaterialCalendar)
            .setTitleText("Выберите период")
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
            val str = "с $startDate по $endDate"
            binding.sortPeriodLabel.text = str
            viewModel.sortPeriodDate = kotlin.Pair(startDate, endDate)
            viewModel.getStatisticsList()
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

