package com.example.agendamentosalao.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.agendamentosalao.R
import com.example.agendamentosalao.database.AppDatabase
import com.example.agendamentosalao.database.daos.AppointmentDao
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.FragmentScheduleAppointmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale
import kotlin.math.log

class ScheduleAppointmentFragment : Fragment(R.layout.fragment_schedule_appointment) {

    private lateinit var binding: FragmentScheduleAppointmentBinding
    private lateinit var database: AppDatabase
    private lateinit var appointmentDao: AppointmentDao
    private lateinit var name: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        appointmentDao = database.appointmentDao()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleAppointmentBinding.bind(view)

        binding.btnSchedule.setOnClickListener {
            if ( fieldValidation() ) {
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
        }

        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                appointmentDao.deleteAll()
            }
        }
    }

    private suspend fun saveAppointment(name: String): Boolean {
        if ( name.isEmpty() || name.isBlank() )
            return false

        appointmentDao.insert(Appointment(name, "26/06/2023 - 15:00", "01/07/23 - 07:00"))
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