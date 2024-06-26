package com.example.agendamentosalao.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.agendamentosalao.database.models.Appointment

@Dao
interface AppointmentDao {

    @Insert
    suspend fun insert(appointment: Appointment)

    @Query("SELECT COUNT(id) FROM appointments")
    suspend fun getTotalItems(): Long

    @Query("DELETE FROM appointments")
    suspend fun deleteAll()


}