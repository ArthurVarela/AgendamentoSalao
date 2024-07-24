package com.example.agendamentosalao.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.repository.AppointmentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() {

    val allAppointments: LiveData<List<Appointment>> = repository.getAllAppointments.asLiveData()

    suspend fun getAppointmentsHours(date: String) : MutableList<String?> = viewModelScope.async {
        repository.getAppointmentsHours(date)
    }.await()

    fun insert(appointment: Appointment) = viewModelScope.launch {
        repository.insert(appointment)
    }

    fun deleteAppointment(appointment: Appointment) = viewModelScope.launch {
        repository.deleteAppointment(appointment)
    }
}

class AppointmentViewModelFactory(private val repository: AppointmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentViewModel (repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}