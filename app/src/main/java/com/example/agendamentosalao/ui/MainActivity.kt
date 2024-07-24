package com.example.agendamentosalao.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.agendamentosalao.R
import com.example.agendamentosalao.application.SalonScheduleApplication
import com.example.agendamentosalao.database.AppDatabase
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.ActivityMainBinding
import com.example.agendamentosalao.databinding.FragmentMainBinding
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModelFactory
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        val navHostFragment = (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)


    }
}