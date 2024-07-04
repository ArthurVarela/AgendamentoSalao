package com.example.agendamentosalao.repository

import androidx.annotation.WorkerThread
import com.example.agendamentosalao.database.daos.AppointmentDao
import com.example.agendamentosalao.database.models.Appointment
import kotlinx.coroutines.flow.Flow

class AppointmentRepository(private val appointmentDao: AppointmentDao) {

    val getAllAppointments: Flow<List<Appointment>> = appointmentDao.getAllAppointments()

    @WorkerThread
    suspend fun insert(appointment: Appointment) {
        appointmentDao.insert( appointment )
    }

    @WorkerThread
    suspend fun deleteAll() {
        appointmentDao.deleteAll()
    }

    @WorkerThread
    suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment)
    }


}