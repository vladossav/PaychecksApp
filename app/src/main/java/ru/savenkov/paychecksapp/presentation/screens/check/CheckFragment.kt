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
import ru.savenkov.paychecksapp.App
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.data
import ru.savenkov.paychecksapp.databinding.FragmentCheckBinding
import ru.savenkov.paychecksapp.presentation.model.Check


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
        val qrRaw = requireArguments().getString(QR_RAW_KEY)
        viewModel.getCheckFromMock()
        //if (!viewModel.getCheck(qrRaw))
            Toast.makeText(context, "Проверьте корректность qr-кода!", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        
        setFragmentResultListener(CategoryDialogFragment.CATEGORY_REQUEST_KEY) { key, bundle ->
            val categoryResult = bundle.getString(CHECK_CATEGORY_KEY)
            Log.d("Category", categoryResult.toString())
        }
        /*viewModel.insertCheck(data)
        viewModel.getAllCheckById(1)*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val goodsAdapter = CheckGoodsAdapter()
        binding.goodsHolder.adapter = goodsAdapter

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_categoryDialogFragment)
        }

        viewModel.checkAll.observe(viewLifecycleOwner) {
            val list = it.checkGoods as ArrayList<Check>
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
       
    }
}