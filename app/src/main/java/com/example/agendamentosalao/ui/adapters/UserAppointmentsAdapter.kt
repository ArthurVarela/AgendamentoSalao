package com.example.agendamentosalao.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.agendamentosalao.database.models.Appointment
import com.example.agendamentosalao.databinding.ItemAppointmentListBinding
import com.example.agendamentosalao.ui.viewmodels.AppointmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserAppointmentsAdapter(
    private val navController: NavController,
    private val appointmentViewModel: AppointmentViewModel
) : Adapter<UserAppointmentsAdapter.UserAppointmentsViewHolder>() {

    private var appointmentsList = emptyList<Appointment>()
    fun addList(list: List<Appointment>){
        appointmentsList = list
        notifyDataSetChanged()
    }

    inner class UserAppointmentsViewHolder(
        private val binding: ItemAppointmentListBinding
    ) : ViewHolder(binding.root){

        fun bind(appointment: Appointment){

            binding.textDay.text = appointment.appointmentDate
            binding.textHour.text = appointment.appointmentHour
            binding.imageButtonDelete.setOnClickListener {
                delete(appointment)
            }
        }

        private fun delete(appointment: Appointment) {

            val context = binding.root.context
            MaterialAlertDialogBuilder(context)
                .setTitle("Cancelar Agendamento")
                .setMessage("Você está prestes a cancelar este agendamento. Deseja continuar?")
                .setNegativeButton("Não"){dialog, position -> }
                .setPositiveButton("Sim, cancelar"){ dialog, position ->
                    appointmentViewModel.deleteAppointment(appointment)
                }
                .show()
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