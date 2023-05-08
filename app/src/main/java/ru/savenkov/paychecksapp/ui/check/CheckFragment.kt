package ru.savenkov.paychecksapp.ui.check

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.savenkov.paychecksapp.databinding.FragmentCheckBinding
import ru.savenkov.paychecksapp.model.CheckItem
import ru.savenkov.paychecksapp.network.ProverkachekaApi
import java.io.IOException

class CheckFragment : Fragment() {
    private var _binding: FragmentCheckBinding? = null
    private val binding get() = _binding!!
    //private val viewModel: CheckViewModel by activityViewModels()
    private lateinit var qrRaw: String


    private val api = ProverkachekaApi.create()

    fun getCheckFromApi(qrRaw: String) = lifecycleScope.launch(Dispatchers.IO) {
        //loading.postValue(true)
        Log.d("CheckApi","input qrraw: $qrRaw")

        val response = try {
            api.getCheck(qrRaw)
        } catch (e: IOException) {
            Log.e("CheckApi", "IOException: ${e.message}")
            //loading.postValue(false)
            return@launch
        } catch (e: HttpException) {
            Log.e("CheckApi", "HttpException: ${e.message}")
            //loading.postValue(false)
            return@launch
        } catch (e: Exception) {
            //loading.postValue(false)
            Log.e("CheckApi",e.message.toString())
            Log.e("CheckApi","Error: Some troubles on server! Try later!")
            return@launch
        }

        if (response.isSuccessful && response.body() != null) {
            val res = response.body()
            val check: CheckItem = res!!
            Log.d("CheckApi", check.toString())
            Log.d("CheckApi", response.message())
        } else {
            Log.e("CheckApi", "Response not successful")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrRaw = requireArguments().getString(QR_RAW_KEY).toString()
        getCheckFromApi(qrRaw)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        binding.textView.text = qrRaw
        return binding.root
    }

    companion object {
       const val QR_RAW_KEY = "QR_RAW_KEY"
    }
}