package com.example.agendamentosalao.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.agendamentosalao.R
import com.example.agendamentosalao.application.SalonScheduleApplication
import com.example.agendamentosalao.databinding.FragmentScheduleAppointmentBinding
import com.example.agendamentosalao.databinding.FragmentUserAppointmentsBinding
import com.example.agendamentosalao.ui.adapters.UserAppointmentsAdapter
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModelFactory

class UserAppointmentsFragment : Fragment(R.layout.fragment_user_appointments) {

    private lateinit var binding: FragmentUserAppointmentsBinding
    private lateinit var userAppointmentsAdapter: UserAppointmentsAdapter

    private val appointmentViewModel: AppointmentViewModel by viewModels {
        AppointmentViewModelFactory((requireActivity().application as SalonScheduleApplication).repository  )
    }

    override fun onStart() {
        super.onStart()

        appointmentViewModel.allAppointments.observe(viewLifecycleOwner, Observer { appointments ->
            if ( appointments.isNotEmpty() ){
                userAppointmentsAdapter.addList(appointments)

            }else{
                Toast.makeText(requireContext(), "Você não possui nenhum agendamento", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserAppointmentsBinding.bind(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        userAppointmentsAdapter = UserAppointmentsAdapter(findNavController(), appointmentViewModel)
        binding.rvUserAppointments.adapter = userAppointmentsAdapter
        binding.rvUserAppointments.layoutManager = LinearLayoutManager(context)
    }
}