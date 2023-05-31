package ru.savenkov.paychecksapp.presentation.screens.check

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.radiobutton.MaterialRadioButton
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.DialogFragmentCategoryCheckBinding
import ru.savenkov.paychecksapp.model.network.data.CheckItem

class CategoryDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentCategoryCheckBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryDialogFragmentViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                CategoryDialogFragmentViewModel((requireActivity().applicationContext as App).repository)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentCategoryCheckBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputCategory.clearFocus()

        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            list.forEach {
                val radioButton = MaterialRadioButton(requireContext())
                radioButton.text = it.name
                binding.radioGroup.addView(radioButton, 0)
            }
        }
        //binding.radioGroup.add
        binding.chooseButton.setOnClickListener {
            val id = binding.radioGroup.checkedRadioButtonId
            if (id == -1) return@setOnClickListener
            var categoryName = ""
            categoryName = if (id == R.id.radio_inputText) binding.inputCategory.text.toString()
            else view.findViewById<MaterialRadioButton>(id).text.toString()
            //Если categoryName = null вылетает exception
            Log.d("Category", categoryName)
            setFragmentResult(
                CATEGORY_REQUEST_KEY,
                bundleOf(CheckFragment.CHECK_CATEGORY_KEY to categoryName)
            )
            dismiss()

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        const val CHECK_ITEM_FROM_API = "CheckItemFromApiToSave"
        const val CATEGORY_REQUEST_KEY = "CATEGORY_REQUEST_KEY"
    }
}