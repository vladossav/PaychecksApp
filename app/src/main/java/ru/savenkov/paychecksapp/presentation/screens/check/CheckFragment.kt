package ru.savenkov.paychecksapp.presentation.screens.check

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.savenkov.paychecksapp.App
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
        /*if (savedInstanceState == null) {
            if (arguments?.containsKey(QR_RAW_KEY)!!) {
                val qrRaw = arguments?.getString(QR_RAW_KEY).toString()
                viewModel.getCheck(qrRaw)
            }
        }*/
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)


        viewModel.insertCheck(data)
        viewModel.getAllCheckById(1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val goodsAdapter = CheckGoodsAdapter()
        binding.goodsHolder.adapter = goodsAdapter

        viewModel.checkAllInfoTuple.observe(viewLifecycleOwner) {
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
    }
}