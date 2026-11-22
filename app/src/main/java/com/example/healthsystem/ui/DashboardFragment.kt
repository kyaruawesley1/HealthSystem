package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.databinding.FragmentDashboardBinding
import com.example.communityhealthsystem.viewmodel.HealthViewModel

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        // Initialize the adapter with BOTH delete and edit click listeners
        val adapter = AppointmentAdapter(
            onDeleteClick = {
                viewModel.deleteAppointment(it)
                Toast.makeText(requireContext(), "Appointment Canceled", Toast.LENGTH_SHORT).show()
            },
            onEditClick = {
                try {
                    val bundle = Bundle().apply {
                        putInt("appointmentId", it.id)
                    }
                    findNavController().navigate(R.id.action_dashboard_to_update, bundle)
                } catch (e: Exception) {
                    // PREVENTS THE CRASH: If the nav action is missing, it shows this toast instead.
                    Toast.makeText(requireContext(), "Error: Please add 'action_dashboard_to_update' inside nav_graph.xml", Toast.LENGTH_LONG).show()
                }
            }
        )

        binding.recyclerViewAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAppointments.adapter = adapter

        // Toggle the Empty State based on the list size and strictly hide the RecyclerView
        viewModel.getMyAppointments().observe(viewLifecycleOwner) { appointments ->
            adapter.submitList(appointments)

            if (appointments.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewAppointments.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewAppointments.visibility = View.VISIBLE
            }
        }

        // Navigation Options
        binding.fabAddAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_book)
        }

        binding.btnViewCatalog.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_catalog)
        }
    }
}