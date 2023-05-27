package ru.savenkov.paychecksapp.ui.check

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.savenkov.paychecksapp.databinding.FragmentCheckBinding


class CheckFragment : Fragment() {
    private var _binding: FragmentCheckBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckViewModel by activityViewModels {
        viewModelFactory {
            initializer {
                CheckViewModel()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        val qrRaw = requireArguments().getString(QR_RAW_KEY).toString()
        //viewModel.getCheckFromApi(qrRaw)
        viewModel.getCheckFromMock()



        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    companion object {
       const val QR_RAW_KEY = "QR_RAW_KEY"
    }
}