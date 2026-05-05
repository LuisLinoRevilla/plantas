package com.example.plantas.onboarding

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.plantas.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidation()
        setupClickListeners()
    }

    private fun setupValidation() {
        binding.btnRecoverPassword.isEnabled = false

        binding.etRecoveryEmail.addTextChangedListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val email = binding.etRecoveryEmail.text.toString().trim()
        val isEmailValid = isValidEmail(email)

        binding.etRecoveryEmail.error = if (email.isEmpty() || isEmailValid) null else "Correo inválido"

        binding.btnRecoverPassword.isEnabled = isEmailValid
    }

    private fun setupClickListeners() {
        // Flecha para regresar
        binding.ivBackForgot.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRecoverPassword.setOnClickListener {
            // Aquí en el futuro conectarás tu base de datos (ej. Firebase)
            // Por ahora, al presionarlo, podemos simplemente regresar al Login
            findNavController().navigateUp()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}