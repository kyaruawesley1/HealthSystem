package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.data.User
import com.example.communityhealthsystem.databinding.FragmentRegisterBinding
import com.example.communityhealthsystem.viewmodel.HealthViewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.btnRegisterSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pin = binding.etPin.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pin.isEmpty()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(User(fullName = name, email = email, pin = pin))
            Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}