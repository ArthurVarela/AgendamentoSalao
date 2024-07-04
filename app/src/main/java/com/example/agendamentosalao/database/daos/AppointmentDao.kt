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

    @Query("DELETE FROM appointments")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

}