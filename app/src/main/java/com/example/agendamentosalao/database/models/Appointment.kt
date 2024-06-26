package com.example.agendamentosalao.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("appointments")
data class Appointment(
    @ColumnInfo("client_name") val clientName: String,
    val todayDateTime: String = "",
    val appointmentDateTime: String = ""
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
