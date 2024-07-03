package com.example.agendamentosalao.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.agendamentosalao.R
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.ItemAppointmentListBinding
import java.util.Calendar

class UserAppointmentsAdapter() : Adapter<UserAppointmentsAdapter.UserAppointmentsViewHolder>() {

    private var appointmentsList = emptyList<Appointment>()
    fun addList(list: List<Appointment>){
        appointmentsList = list
        notifyDataSetChanged()
    }


    inner class UserAppointmentsViewHolder(
        private val binding: ItemAppointmentListBinding
    ) : ViewHolder(binding.root){

        private val calendar = Calendar.getInstance()

        fun bind(appointment: Appointment){

            binding.textMonthYear.text = calendar.get(Calendar.MONTH + 1).toString()
            binding.textDay.text = appointment.appointmentDate
            binding.textHour.text = appointment.appointmentHour
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAppointmentsViewHolder {
        val itemView = ItemAppointmentListBinding.inflate(
            LayoutInflater.from( parent.context ), parent, false
        )

        return UserAppointmentsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserAppointmentsViewHolder, position: Int) {
        val appointment = appointmentsList[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }
}