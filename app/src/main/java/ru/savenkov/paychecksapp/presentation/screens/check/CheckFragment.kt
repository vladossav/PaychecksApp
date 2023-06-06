package ru.savenkov.paychecksapp.presentation.screens.check

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
            val qrRaw = requireArguments().getString(QR_RAW_KEY)
            if (qrRaw == "mock") viewModel.getCheckFromMock()
            /*if (!viewModel.getCheck(qrRaw))
                Toast.makeText(context, "Проверьте корректность qr-кода!", Toast.LENGTH_LONG).show()*/
            Log.d("CheckFragment", qrRaw.toString())
        }

        if (arguments?.containsKey(CHECK_ID_KEY) == true) {
            val checkId = requireArguments().getLong(CHECK_ID_KEY)
            viewModel.getCheckById(checkId)
            Log.d("CheckFragment", checkId.toString())
        }

        setFragmentResultListener(CategoryDialogFragment.CATEGORY_REQUEST_KEY) { _, bundle ->
            viewModel.checkCategory.value = bundle.getString(CHECK_CATEGORY_KEY)
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
        binding.goodsHolder.adapter = goodsAdapter

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.categoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_categoryDialogFragment,
            bundleOf(CHECK_CATEGORY_KEY to viewModel.checkCategory.value))
        }

        binding.saveButton.setOnClickListener {
            binding.saveButton.text = "Сохранено!"
            binding.saveButton.isClickable = false
            viewModel.saveCheck()
        }

        viewModel.checkAll.observe(viewLifecycleOwner) {
            val list = it.checkGoods as ArrayList<CheckAdapterItem>
            list.add(0, it.checkInfo)
            list.add(it.checkInfo)
            goodsAdapter.checkList = list
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