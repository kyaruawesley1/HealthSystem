package com.example.healthsystem.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.communityhealthsystem.data.Appointment
import com.example.communityhealthsystem.databinding.ItemAppointmentBinding

class AppointmentAdapter(
    private val onDeleteClick: (Appointment) -> Unit,
    private val onEditClick: (Appointment) -> Unit
) : ListAdapter<Appointment, AppointmentAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appt = getItem(position)

        // Fixed: Swapped to 'tvService' to match your XML layout perfectly
        holder.binding.tvService.text = appt.serviceType
        holder.binding.tvDate.text = "Date: ${appt.date}"
        holder.binding.tvTime.text = "Time: ${appt.time}"

        // Fixed: Swapped to 'btnDelete' to match your XML layout perfectly
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(appt) }
        holder.binding.btnEdit.setOnClickListener { onEditClick(appt) }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Appointment>() {
            override fun areItemsTheSame(old: Appointment, new: Appointment) = old.id == new.id
            override fun areContentsTheSame(old: Appointment, new: Appointment) = old == new
        }
    }
}