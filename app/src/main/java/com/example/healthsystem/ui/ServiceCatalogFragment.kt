package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.databinding.FragmentServiceCatalogBinding

class ServiceCatalogFragment : Fragment(R.layout.fragment_service_catalog) {
    private lateinit var binding: FragmentServiceCatalogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentServiceCatalogBinding.bind(view)

        // Allows the user to return to the Dashboard
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}