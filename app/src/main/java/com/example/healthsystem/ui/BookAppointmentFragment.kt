package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.data.Appointment
import com.example.communityhealthsystem.databinding.FragmentBookAppointmentBinding
import com.example.communityhealthsystem.viewmodel.HealthViewModel
import com.example.communityhealthsystem.worker.ReminderWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookAppointmentFragment : Fragment(R.layout.fragment_book_appointment) {
    private lateinit var binding: FragmentBookAppointmentBinding
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookAppointmentBinding.bind(view)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val services = arrayOf(
            "Health Education",
            "Maternal/Child Wellness",
            "Fitness Tracking",
            "Immunization & Vaccination",
            "Mental Health Counseling"
        )
        binding.spinnerServices.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, services)

        binding.btnSaveBooking.setOnClickListener {
            val service = binding.spinnerServices.selectedItem.toString()
            val date = binding.etBookingDate.text.toString().trim()
            val time = binding.etBookingTime.text.toString().trim()

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isValidFutureDateTime(date, time)) {
                viewModel.addAppointment(
                    Appointment(patientId = viewModel.currentUserId, serviceType = service, date = date, time = time)
                )

                val request = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInputData(workDataOf("SERVICE_NAME" to service))
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build()
                WorkManager.getInstance(requireContext()).enqueue(request)

                Toast.makeText(requireContext(), "Appointment Booked Successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Invalid time. Please use DD/MM/YYYY and a future time.", Toast.LENGTH_LONG).show()
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