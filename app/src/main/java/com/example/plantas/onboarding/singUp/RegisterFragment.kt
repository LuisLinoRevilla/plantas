package com.example.plantas.onboarding.singUp

import android.content.Context
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
import com.example.plantas.databinding.FragmentRegisterBinding
import com.example.plantas.signup.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // 1. Conectamos el ViewModel
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var communicator: FragmentCommunicator

    // 2. Conectamos el comunicador para la pantalla de carga (sábana blanca)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            communicator = context as FragmentCommunicator
        } catch (e: ClassCastException) {
            throw ClassCastException("$context debe implementar FragmentCommunicator")
        }
    }

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
        observeState() // 3. Iniciamos el observador
    }

    private fun setupValidation() {
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
            // 4. En lugar de navegar directo, llamamos a Firebase
            val email = binding.etEmailReg.text.toString().trim()
            val password = binding.etPasswordReg.text.toString().trim()
            viewModel.requestSignUp(email, password)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.btnRegister.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            binding.btnRegister.isEnabled = true

                            // 5. ¡Si Firebase dice que todo salió bien, AHORA SÍ navegamos!
                            findNavController().navigate(R.id.action_registerFragment_to_registerInfoFragment)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.btnRegister.isEnabled = true

                            // Mostramos el error (Ej: "El correo ya está registrado")
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
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