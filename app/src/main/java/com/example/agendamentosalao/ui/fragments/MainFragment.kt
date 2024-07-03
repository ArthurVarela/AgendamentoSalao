package com.example.agendamentosalao.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.agendamentosalao.R
import com.example.agendamentosalao.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.btnMainSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_scheduleAppointmentFragment)
        }

    }

}