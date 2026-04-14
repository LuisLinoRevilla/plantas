package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.plantas.databinding.FragmentRegisterInfoBinding

class RegisterInfoFragment : Fragment() {

    private var _binding: FragmentRegisterInfoBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupValidation() {
        binding.btnFinishRegister.isEnabled = false

        binding.etPhone.addTextChangedListener { validateFields() }
        binding.etFavoritePlant.addTextChangedListener { validateFields() }
    }

    private fun validateFields() {
        val phone = binding.etPhone.text.toString().trim()
        val plant = binding.etFavoritePlant.text.toString().trim()

        val isPhoneValid = phone.length >= 10
        val isPlantValid = plant.isNotEmpty()

        binding.etPhone.error = if (phone.isEmpty() || isPhoneValid) null else "Mínimo 10 dígitos"
        binding.etFavoritePlant.error = if (isPlantValid || plant.isEmpty()) null else "Ingresa tu planta favorita"

        binding.btnFinishRegister.isEnabled = isPhoneValid && isPlantValid
    }

    private fun setupClickListeners() {
        binding.ivBackInfo.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFinishRegister.setOnClickListener {

            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}