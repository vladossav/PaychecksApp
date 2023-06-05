package ru.savenkov.paychecksapp.presentation.screens.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.FragmentSavedBinding
import ru.savenkov.paychecksapp.presentation.screens.check.CheckFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.getCheckList()
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
            viewModel.selectedCategory.value = category
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            binding.selectedCategory.isVisible = true
            binding.selectedCategory.text = it
        }

        binding.savedHolder.adapter = savedChecksAdapter
        binding.categoriesHolder.adapter = categoryAdapter
        binding.selectedCategory.setOnCloseIconClickListener {
            viewModel.getCheckList()
            it.isVisible = false
        }


        viewModel.checksList.observe(viewLifecycleOwner) {
            savedChecksAdapter.checkList = it
        }

        viewModel.categoryList.observe(viewLifecycleOwner) {
            categoryAdapter.categoryList = it as ArrayList<String>
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}