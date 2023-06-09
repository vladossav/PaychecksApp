package ru.savenkov.paychecksapp.presentation.screens.check

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.FragmentCheckBinding
import ru.savenkov.paychecksapp.presentation.model.CheckAdapterItem

class CheckFragment : Fragment() {
    private var _binding: FragmentCheckBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                CheckViewModel((requireActivity().applicationContext as App).repository)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(QR_RAW_KEY) == true) {
            viewModel.checkSavedState.value = CheckSavedState.NOT_SAVED
            val qrRaw = requireArguments().getString(QR_RAW_KEY)
            if (qrRaw == "mock") viewModel.getCheckFromMock()
            /*if (!viewModel.getCheck(qrRaw))
                Toast.makeText(context, "Проверьте корректность qr-кода!", Toast.LENGTH_LONG).show()*/
            Log.d("CheckFragment", qrRaw.toString())
        }

        if (arguments?.containsKey(CHECK_ID_KEY) == true) {
            viewModel.checkSavedState.value = CheckSavedState.SAVED
            val checkId = requireArguments().getLong(CHECK_ID_KEY)
            viewModel.getCheckById(checkId)
            Log.d("CheckFragment", checkId.toString())
        }

        setFragmentResultListener(CategoryDialogFragment.CATEGORY_REQUEST_KEY) { _, bundle ->
            viewModel.checkCategory.value = bundle.getString(CHECK_CATEGORY_KEY)
            if (viewModel.checkSavedState.value == CheckSavedState.SAVED) {
                viewModel.updateCheckCategory()
            }
            Log.d("Category", "CheckFragmentResult: " + viewModel.checkCategory.value)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val goodsAdapter = CheckGoodsAdapter()
        binding.checkHolder.adapter = goodsAdapter

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.categoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_categoryDialogFragment,
            bundleOf(CHECK_CATEGORY_KEY to viewModel.checkCategory.value))
        }

        binding.saveDeleteButton.setOnClickListener {
            viewModel.makeSaveDeleteAction()
        }

        viewModel.checkName.observe(viewLifecycleOwner) {
            binding.checkName.setText(it, TextView.BufferType.EDITABLE)
        }

        viewModel.checkSavedState.observe(viewLifecycleOwner) { state->
            if (state == CheckSavedState.SAVED) {
                binding.saveDeleteButton.text = resources.getString(R.string.check_delete_button)
            }
            else {
                binding.saveDeleteButton.text = resources.getString(R.string.check_save_button)
            }
        }

        setCheckEditNameSettings()

        viewModel.checkAll.observe(viewLifecycleOwner) {
            val list = it.checkGoods as ArrayList<CheckAdapterItem>
            list.add(0, it.checkInfo)
            list.add(it.checkInfo)
            goodsAdapter.checkList = list
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setCheckEditNameSettings() {
        binding.checkHolder.setOnTouchListener { _, _ ->
            if (activity?.currentFocus != null) {
                val inputManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            binding.checkName.isEnabled = false
            binding.checkName.clearFocus()
            false
        }

        binding.checkName.setOnFocusChangeListener { _, _ ->
            viewModel.checkName.value = binding.checkName.text.toString()
            Log.d("CheckFragment", binding.checkName.text.toString())
            if (viewModel.checkSavedState.value == CheckSavedState.SAVED) {
                viewModel.updateCheckName()
            }
        }

        binding.editButton.setOnClickListener {
            binding.checkName.isEnabled = true
            binding.checkName.requestFocus()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
       const val QR_RAW_KEY = "QR_RAW_KEY"
       const val CHECK_CATEGORY_KEY = "CheckNewCategoryName"
       const val CHECK_ID_KEY = "CHECK_ID_KEY"
    }
}