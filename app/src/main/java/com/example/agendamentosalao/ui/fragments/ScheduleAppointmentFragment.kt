package com.example.agendamentosalao.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.agendamentosalao.R
import com.example.agendamentosalao.application.SalonScheduleApplication
import com.example.agendamentosalao.database.AppDatabase
import com.example.agendamentosalao.database.daos.AppointmentDao
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.FragmentScheduleAppointmentBinding
import com.example.agendamentosalao.repository.AppointmentRepository
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleAppointmentFragment : Fragment(R.layout.fragment_schedule_appointment) {

    private lateinit var binding: FragmentScheduleAppointmentBinding
    private lateinit var name: String
    private lateinit var calendar: Calendar

    private val appointmentViewModel: AppointmentViewModel by viewModels {
        AppointmentViewModelFactory((requireActivity().application as SalonScheduleApplication).repository  )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendar = Calendar.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleAppointmentBinding.bind(view)

        binding.btnSchedule.setOnClickListener {
            if ( fieldValidation() ) {
                initializeAlertDialogAndSave()
            }
        }

        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                appointmentViewModel.deleteAll()
            }
        }

        binding.btnTeste.setOnClickListener {

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDateTime = dateFormat.format(calendar.time)

            Log.i("", "data formatada: $formattedDateTime")
        }
    }

    private fun initializeAlertDialogAndSave() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmação de Agendamento")
            .setMessage("Voce está agendando para 01/07/23 - 07:00, voce tem certeza disso ?")
            .setNegativeButton("Não, quero outra data"){ dialog, position -> }
            .setPositiveButton("Agendar"){dialog, position ->
                CoroutineScope(Dispatchers.IO).launch {

                    val result = saveAppointment(name)

                    withContext(Dispatchers.Main){
                        if (result) {
                            Toast.makeText(requireContext(), "Horario agendado com sucesso", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_scheduleAppointmentFragment_to_mainFragment)
                        }else{
                            Toast.makeText(requireContext(), "Falha ao agendar horario", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .show()
    }

    private fun saveAppointment(name: String): Boolean {
        if ( name.isEmpty() || name.isBlank() )
            return false

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDateTime = dateFormat.format(calendar.time)
        appointmentViewModel.insert(Appointment(name, formattedDateTime, "20/07", "08:00"))
        return  true
    }

    private fun fieldValidation(): Boolean {
        name = binding.EditName.text.toString().trim()
        Log.i("teste", "fieldValidation: '$name' ")

        return if ( name.isNotEmpty() ){
            binding.textInputName.error = null
            true
        }else{
            binding.textInputName.error = "Porfavor preencha seu nome"
            false
        }
    }

}