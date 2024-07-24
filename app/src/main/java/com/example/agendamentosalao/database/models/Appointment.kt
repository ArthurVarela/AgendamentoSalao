package com.example.agendamentosalao.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("appointments")
data class Appointment(
    @ColumnInfo("client_name") val clientName: String,
    @ColumnInfo("created_at") val todayDateTime: String,
    @ColumnInfo("date") val appointmentDate: String,
    @ColumnInfo("hour") val appointmentHour: String?,
    @ColumnInfo("service") val appointmentService: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}