package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.plantas.core.FragmentCommunicator
import com.example.plantas.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SigninViewModel>()

    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator.manageLoader(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidation()
        setupClickListeners()
    }

    private fun setupValidation() {
        binding.btnLogin.isEnabled = false

        binding.etEmail.addTextChangedListener {
            validateFields()
        }
        binding.etPassword.addTextChangedListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        binding.etEmail.error = if (email.isEmpty() || isEmailValid) null else "Correo inválido"
        binding.etPassword.error = if (password.isEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        binding.btnLogin.isEnabled = isEmailValid && isPasswordValid
    }

    private fun setupClickListeners() {
        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}