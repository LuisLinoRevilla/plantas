package com.example.plantas.singup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.plantas.R
import com.example.plantas.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidation()
        setupClickListeners()
    }

    private fun setupValidation() {
        // El botón comienza deshabilitado
        binding.btnRegister.isEnabled = false

        binding.etName.addTextChangedListener { validateFields() }
        binding.etEmailReg.addTextChangedListener { validateFields() }
        binding.etPasswordReg.addTextChangedListener { validateFields() }
    }

    private fun validateFields() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmailReg.text.toString().trim()
        val password = binding.etPasswordReg.text.toString().trim()

        val isNameValid = name.isNotEmpty()
        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        binding.etName.error = if (isNameValid || name.isEmpty()) null else "El nombre es requerido"
        binding.etEmailReg.error = if (email.isEmpty() || isEmailValid) null else "Correo inválido"
        binding.etPasswordReg.error = if (password.isEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        binding.btnRegister.isEnabled = isNameValid && isEmailValid && isPasswordValid
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerInfoFragment)
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