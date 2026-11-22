package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.databinding.FragmentUpdateAppointmentBinding
import com.example.communityhealthsystem.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateAppointmentFragment : Fragment(R.layout.fragment_update_appointment) {
    private lateinit var binding: FragmentUpdateAppointmentBinding
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateAppointmentBinding.bind(view)

        val appointmentId = arguments?.getInt("appointmentId", -1) ?: -1

        binding.btnCancelReschedule.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnConfirmReschedule.setOnClickListener {
            val newDate = binding.etNewDate.text.toString().trim()
            val newTime = binding.etNewTime.text.toString().trim()

            if (newDate.isNotEmpty() && newTime.isNotEmpty() && appointmentId != -1) {

                if (isValidFutureDateTime(newDate, newTime)) {
                    viewModel.updateAppointment(appointmentId, newDate, newTime)
                    Toast.makeText(requireContext(), "Appointment Rescheduled", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "Invalid time. Please use DD/MM/YYYY and a future time.", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidFutureDateTime(dateString: String, timeString: String): Boolean {
        return try {
            // Automatically converts formats like "10:00pm" into "10:00 PM" for the parser
            var cleanTime = timeString.trim().uppercase(Locale.ENGLISH)
            cleanTime = cleanTime.replace(" ", "") // Remove any accidental spaces
            cleanTime = cleanTime.replace("AM", " AM").replace("PM", " PM") // Ensure exact single space

            val dateTimeString = "$dateString $cleanTime"

            val sdf = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.ENGLISH)
            sdf.isLenient = false
            val inputDate = sdf.parse(dateTimeString)

            val now = Calendar.getInstance().time

            inputDate != null && inputDate.after(now)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}