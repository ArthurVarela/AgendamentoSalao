package com.example.agendamentosalao.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("appointments")
data class Appointments(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("client_name") val clientName: String,
    val todayDate: String,
    val appointmentDate: String
)
