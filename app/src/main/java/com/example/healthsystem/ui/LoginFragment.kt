package com.example.healthsystem.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.communityhealthsystem.R
import com.example.communityhealthsystem.databinding.FragmentLoginBinding
import com.example.communityhealthsystem.viewmodel.HealthViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.tvGoToRegister.setOnClickListener { findNavController().navigate(R.id.action_login_to_register) }

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val pin = binding.etLoginPin.text.toString().trim()

            if (email.isEmpty() || pin.isEmpty()) {
                Toast.makeText(requireContext(), "Enter credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = viewModel.login(email, pin)
                if (user != null) {
                    viewModel.currentUserId = user.id
                    findNavController().navigate(R.id.action_login_to_dashboard)
                } else {
                    Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}