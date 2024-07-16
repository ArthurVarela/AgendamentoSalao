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

    @Query("SELECT COUNT(id) FROM appointments")
    suspend fun getTotalItems(): Long

    @Query("SELECT * FROM appointments")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT COUNT(*) FROM appointments WHERE appointment_date = :date AND appointment_hour = :hour")
    suspend fun countAppointmentsByDateAndHour(date: String, hour: String): Int

    @Query("SELECT appointment_hour FROM appointments WHERE appointment_date = :date")
    suspend fun getAppointmentsHours(date: String) : MutableList<String?>

    @Query("DELETE FROM appointments")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

}