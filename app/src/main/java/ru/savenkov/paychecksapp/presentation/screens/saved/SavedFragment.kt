package ru.savenkov.paychecksapp.presentation.screens.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.databinding.FragmentSavedBinding
import ru.savenkov.paychecksapp.presentation.screens.check.CheckViewModel

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

        val adapter = SavedChecksAdapter()
        binding.savedHolder.adapter = adapter

        viewModel.checksList.observe(viewLifecycleOwner) {
            adapter.checkList = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}