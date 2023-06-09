package ru.savenkov.paychecksapp.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.savenkov.paychecksapp.R
import ru.savenkov.paychecksapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavGraph()
    }

    private fun setNavGraph() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.let { binding.navView.setSetupWithNavController(it) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Permission was denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun BottomNavigationView.setSetupWithNavController(navController: NavController?) {
        navController?.let {
            setupWithNavController(it)
        }
        setOnItemSelectedListener { menuItem ->
            val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(false)
            val graph = navController?.currentDestination?.parent
            val destination = graph?.findNode(menuItem.itemId)
            if (menuItem.order and Menu.CATEGORY_SECONDARY == 0) {
                navController?.graph?.findStartDestination()?.id?.let {
                    builder.setPopUpTo(
                        it,
                        inclusive = false,
                        saveState = true
                    )
                }
            }
            val options = builder.build()
            destination?.id?.let { id -> navController.navigate(id, null, options) }
            return@setOnItemSelectedListener true
        }
    }

}