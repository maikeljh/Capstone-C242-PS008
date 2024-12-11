package com.example.culinairy

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.culinairy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.culinairy.repository.TransactionRepository
import com.example.culinairy.ui.dashboard.DashboardViewModel
import com.example.culinairy.services.RetrofitInstance
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var navController: NavController
    private val noBackPressFragments = setOf(
        R.id.navigation_home,
        R.id.navigation_capture_receipt_success
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_capture_receipt,
                R.id.navigation_product,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        fab = binding.fab
        fab.setOnClickListener {
            if (navController.currentDestination?.id != R.id.navigation_capture_receipt) {
                navController.navigate(R.id.navigation_capture_receipt)
                updateBottomNavSelection(R.id.navigation_capture_receipt)
            }
        }

        navView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_product -> {
                    navController.navigate(R.id.navigation_product)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }.also { isHandled ->
                if (isHandled) updateBottomNavSelection(item.itemId)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = navController.currentDestination?.id
                if (currentDestinationId in noBackPressFragments) {
                    // Disable back press
                } else {
                    if (!navController.navigateUp()) {
                        finish()
                    } else {
                        updateBottomNavSelection(
                            navController.currentDestination?.id ?: R.id.navigation_home
                        )
                    }
                }
            }
        })
    }

    fun updateBottomNavSelection(itemId: Int) {
        val navView: BottomNavigationView = binding.navView
        navView.menu.findItem(itemId).isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
