package ru.savenkov.paychecksapp.presentation.screens.scan

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.google.zxing.BarcodeFormat
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.FragmentScanBinding
import ru.savenkov.paychecksapp.presentation.screens.check.CheckFragment

class ScanFragment : Fragment() {
    private var binding: FragmentScanBinding? = null
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
             != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                200
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        initQrScanner()

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun initQrScanner() {
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, binding!!.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE
            decodeCallback = DecodeCallback {
                releaseResources()
                activity.runOnUiThread {
                    findNavController().navigate(
                        R.id.action_navigation_scan_to_checkFragment,
                        bundleOf(CheckFragment.QR_RAW_KEY to it.text)
                    )
                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                }
            }
            errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding!!.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
}