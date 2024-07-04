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
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.FragmentScheduleAppointmentBinding
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ScheduleAppointmentFragment : Fragment(R.layout.fragment_schedule_appointment) {

    private lateinit var binding: FragmentScheduleAppointmentBinding
    private lateinit var name: String
    private lateinit var appointmentDate: String
    private lateinit var appointmentHour: String
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
            try {
                appointmentViewModel.deleteAll()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.textInputDateLayout.setEndIconOnClickListener {
            showDatePicker()
        }

        /*binding.editDate.setOnClickListener {
            showDatePicker()
        }*/
    }

    private fun showDatePicker() {
        val constraintBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraintBuilder)
                .build()

        datePicker.show(parentFragmentManager, "teste")

    }


    private fun initializeAlertDialogAndSave() {

        name = binding.EditName.text.toString()
        appointmentDate = "30/07"
        appointmentHour = "14:00"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDateTime = dateFormat.format(calendar.time)
        val appointment = Appointment(name, formattedDateTime, appointmentDate, appointmentHour)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmação de Agendamento")
            .setMessage("Voce está agendando para  - $appointmentDate - $appointmentHour, voce tem certeza disso ?")
            .setNegativeButton("Não, quero outra data"){ dialog, position -> }
            .setPositiveButton("Agendar"){ dialog, position ->
                CoroutineScope(Dispatchers.IO).launch {

                    val result = saveAppointment(appointment)

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

    private fun saveAppointment(appointment: Appointment): Boolean {
        return try {
            appointmentViewModel.insert(appointment)
             true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
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