package com.example.plantas.onboarding.singIn

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.plantas.R
import com.example.plantas.core.FragmentCommunicator
import com.example.plantas.core.ResponseService
import com.example.plantas.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
        // Inicializar communicator - tu Activity debe implementar esta interfaz
        communicator = activity as FragmentCommunicator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidation()
        setupClickListeners()
        observeState()
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
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.requestLogin(email, password) // ✅ Llamar al login
        }

        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signInState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.btnLogin.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            // TODO: navegar a MainActivity
                            Snackbar.make(binding.root, "Login exitoso", Snackbar.LENGTH_LONG).show()
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.btnLogin.isEnabled = true
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}