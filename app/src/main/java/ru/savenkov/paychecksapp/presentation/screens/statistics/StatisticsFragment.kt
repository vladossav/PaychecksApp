package ru.savenkov.paychecksapp.presentation.screens.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.FragmentStatisticsBinding


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

        if (savedInstanceState == null) viewModel.getStatisticsList(SortType.BY_DESC)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatisticAdapter { sortMenuId ->
            when (sortMenuId) {
                R.id.sort_asc -> viewModel.getStatisticsList(SortType.BY_ASC)
                R.id.sort_desc -> viewModel.getStatisticsList(SortType.BY_DESC)
            }
        }
        binding.statisticsHolder.adapter = adapter

        viewModel.statisticList.observe(viewLifecycleOwner) {
            adapter.statisticList = it
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

