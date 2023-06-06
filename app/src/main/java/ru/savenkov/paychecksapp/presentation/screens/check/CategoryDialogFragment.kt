package ru.savenkov.paychecksapp.presentation.screens.check

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup.LayoutParams
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

        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            fillRadioGroup(list)
        }

        binding.chooseButton.setOnClickListener {
            saveRadioCategoryAndExit(view)
        }
    }

    private fun fillRadioGroup(list: List<String>) {
        if (binding.radioGroup.childCount >= list.size + 2) return
        var savedCategory: String? = null
        if (arguments?.containsKey(CheckFragment.CHECK_CATEGORY_KEY) == true) {
            savedCategory = requireArguments().getString(CheckFragment.CHECK_CATEGORY_KEY)
        }

        val heightAtDp = (35 * resources.displayMetrics.density).toInt()
        list.forEach {category ->
            val radioButton = MaterialRadioButton(requireContext()).apply {
                layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, heightAtDp)
                text = category
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }
            binding.radioGroup.addView(radioButton, 0)
            if (category == savedCategory) binding.radioGroup.check(radioButton.id)
        }
        if (savedCategory == null) binding.radioGroup.check(R.id.radio_no_category)
    }

    private fun saveRadioCategoryAndExit(view: View) {
        val id = binding.radioGroup.checkedRadioButtonId
        val categoryName: String? = when(id) {
            -1 -> null
            R.id.radio_no_category -> null
            R.id.radio_inputText -> {
                val inputCategory = binding.inputCategory.text.toString().lowercase()
                if (inputCategory == "" || inputCategory == " ") return
                inputCategory
            }
            else -> view.findViewById<MaterialRadioButton>(id).text.toString()
        }

        Log.d("Category", categoryName.toString())
        setFragmentResult(
            CATEGORY_REQUEST_KEY,
            bundleOf(CheckFragment.CHECK_CATEGORY_KEY to categoryName)
        )
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val CATEGORY_REQUEST_KEY = "CATEGORY_REQUEST_KEY"
    }
}