package com.example.agendamentosalao.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.agendamentosalao.database.models.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Insert
    suspend fun insert(appointment: Appointment)

    @Query("SELECT * FROM appointments")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT hour FROM appointments WHERE date = :date")
    suspend fun getAppointmentsHours(date: String) : MutableList<String?>

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

}