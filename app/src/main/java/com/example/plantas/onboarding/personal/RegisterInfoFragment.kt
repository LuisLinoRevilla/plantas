package com.example.plantas.onboarding.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.plantas.core.ResponseService
import com.example.plantas.databinding.FragmentRegisterInfoBinding
import com.example.plantas.home.HomeActivity
import com.example.plantas.onboarding.personal.model.RegisterInfoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterInfoFragment : Fragment() {

    private var _binding: FragmentRegisterInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidation()
        setupClickListeners()
        observeSaveState()
    }

    private fun setupValidation() {
        binding.btnFinishRegister.isEnabled = false
        binding.etPhone.addTextChangedListener { validateFields() }
        binding.etLastName.addTextChangedListener { validateFields() }
        binding.etFavoritePlant.addTextChangedListener { validateFields() }
    }

    private fun validateFields() {
        val phone = binding.etPhone.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val plant = binding.etFavoritePlant.text.toString().trim()

        val isPhoneValid = phone.length >= 10
        val isLastNameValid = lastName.isNotEmpty()
        val isPlantValid = plant.isNotEmpty()

        binding.btnFinishRegister.isEnabled = isPhoneValid && isLastNameValid && isPlantValid
    }

    private fun setupClickListeners() {
        binding.ivBackInfo.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFinishRegister.setOnClickListener {
            // 1. Recuperamos el nombre que llegó por argumentos
            val nombreRecibido = arguments?.getString("nombre_usuario") ?: ""

            // 2. Obtenemos el resto de campos
            val lastName = binding.etLastName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val favoritePlant = binding.etFavoritePlant.text.toString().trim()

            // 3. Enviamos los datos correctos al ViewModel
            viewModel.guardarDatosUsuario(nombreRecibido, lastName, phone, favoritePlant)
        }
    }

    private fun observeSaveState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveState.collect { state ->
                when (state) {
                    is ResponseService.Loading -> {
                        binding.btnFinishRegister.isEnabled = false
                    }
                    is ResponseService.Success -> {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    is ResponseService.Error -> {
                        binding.btnFinishRegister.isEnabled = true
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}