package com.example.agendamentosalao.ui.fragments

import android.database.CrossProcessCursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.agendamentosalao.R
import com.example.agendamentosalao.application.SalonScheduleApplication
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.FragmentScheduleAppointmentBinding
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ScheduleAppointmentFragment : Fragment(R.layout.fragment_schedule_appointment) {

    private lateinit var binding: FragmentScheduleAppointmentBinding
    private lateinit var name: String
    private lateinit var appointmentDate: String
    private var appointmentHour: String? = null
    //private lateinit var date: String
    private lateinit var newAppointment: Appointment

    private val appointmentViewModel: AppointmentViewModel by viewModels {
        AppointmentViewModelFactory((requireActivity().application as SalonScheduleApplication).repository  )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleAppointmentBinding.bind(view)

        binding.btnSchedule.setOnClickListener {
            if ( fieldValidation() ) {
                if (appointmentHour != null){
                    initializeAlertDialogAndSave()
                }else{
                    Toast.makeText(requireContext(), "Escolha um dos horários disponíveis!", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.btnDelete.setOnClickListener {
            try {
                appointmentViewModel.deleteAll()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.editDate.setOnClickListener {
            binding.textInputDate.error = null
            showDatePicker()
        }

        binding.textInputDate.setEndIconOnClickListener {
            showDatePicker()
        }

        binding.teste.setOnClickListener {

        }
    }

    private fun showDatePicker() {

        //set the constraints, to the availables dates to select be only the dates forward
        val constraintBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

        //Build the datePicker with the constraints
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione uma data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintBuilder)
                .build()

        datePicker.show(parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->

            //Converting selected date in miliseconds to a LocalDate
            val selectedDate = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate()

            //format selected Date
            appointmentDate = DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(selectedDate)

            binding.editDate.setText(appointmentDate)

            showHoursAvailable(appointmentDate)
        }

    }

    private fun showHoursAvailable(date: String) {

        //create a list of hours that are available to schedule
        val hoursAvailable = mutableListOf(
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00"
        )

        CoroutineScope(Dispatchers.IO).launch {
            val hoursNotAvailable = appointmentViewModel.getAppointmentsHours(date)

            withContext(Dispatchers.Main) {

                binding.gridLayout.removeAllViews()

                //remove hours that already exists from the hours available list
                hoursNotAvailable.forEach { hour ->
                    hoursAvailable.remove(hour)
                }


                // Create buttons just for the hours that are available
                hoursAvailable.forEach { hour ->
                    val button = MaterialButton(
                        requireContext(),
                        null,
                        com.google.android.material.R.attr.materialButtonOutlinedStyle
                    ).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        text = hour
                        setOnClickListener {
                            selectedHour(hour)
                        }
                    }
                    binding.gridLayout.addView(button)
                }
            }
        }
    }

    private fun selectedHour(hour: String) {
        clearSelection()

        //highlight the selected Button
        binding.gridLayout.forEach { view ->
            if (view is MaterialButton && view.text == hour){
                view.setBackgroundColor(requireContext().getColor(R.color.primary))
                view.setTextColor(requireContext().getColor(R.color.white))
                appointmentHour = hour
            }
        }
    }

    private fun clearSelection() {
        //remove highlight from all buttons
        binding.gridLayout.forEach {view ->
            if (view is MaterialButton) {
                view.setTextColor(requireContext().getColor(R.color.primary))
                view.setBackgroundColor(requireContext().getColor(android.R.color.transparent))
            }
        }
        //clear selectedHour
        appointmentHour = ""
    }

    private fun initializeAlertDialogAndSave() {

        name = binding.EditName.text.toString()
        appointmentDate.trim()

        //Get Current Date Time
        val currentDateTime = LocalDateTime.now()
        val formattedCurrentDateTime = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .format(currentDateTime)

        newAppointment = Appointment(name, formattedCurrentDateTime, appointmentDate, appointmentHour)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmação de Agendamento")
            .setMessage("Você está agendando para as $appointmentHour do dia $appointmentDate. Você tem certeza disso?")
            .setNegativeButton("Cancelar"){ dialog, position -> }
            .setPositiveButton("Agendar"){ dialog, position ->

                val result = saveAppointment(newAppointment)

                if (result) {
                    Toast.makeText(requireContext(), "Horário agendado com sucesso.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_scheduleAppointmentFragment_to_mainFragment)
                }else{
                    Toast.makeText(requireContext(), "Falha ao agendar horário.", Toast.LENGTH_SHORT).show()
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
        appointmentDate = binding.editDate.text.toString().trim()

        return if ( name.isNotEmpty() ){
            binding.textInputName.error = null

            if ( appointmentDate.isNotEmpty() ){

                binding.textInputDate.error = null
                true
            }else{

                binding.textInputDate.error = "Por favor, selecione uma data válida."
                false
            }
        }else{

            binding.textInputName.error = "Por favor, preencha seu nome."
            false
        }
    }
}