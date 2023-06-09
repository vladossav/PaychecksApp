package ru.savenkov.paychecksapp.presentation.screens.saved

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.savenkov.paychecksapp.databinding.DialogSavedSortSumBinding

class SortSumDialogFragment: DialogFragment() {
    private var _binding: DialogSavedSortSumBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSavedSortSumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseButton.setOnClickListener {
            val from = binding.inputSumFrom.text.toString()
            val till = binding.inputSumTill.text.toString()
            if (from.isBlank() || till.isBlank() || from.toLong() >= till.toLong()) return@setOnClickListener
            setFragmentResult(
                SORT_TOTAL_SUM_REQUEST_KEY,
                bundleOf(SORT_TOTAL_SUM_FROM_KEY to from,
                    SORT_TOTAL_SUM_TILL_KEY to till)
            )
            dismiss()
        }
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
        const val SORT_TOTAL_SUM_REQUEST_KEY = "SORT_TOTAL_SUM_REQUEST_KEY"
        const val SORT_TOTAL_SUM_FROM_KEY = "SORT_TOTAL_SUM_FROM_KEY"
        const val SORT_TOTAL_SUM_TILL_KEY = "SORT_TOTAL_SUM_TILL_KEY"
    }
}