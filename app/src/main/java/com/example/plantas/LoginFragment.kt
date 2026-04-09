package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private var _binding: FragmentLogingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun setupValidation(){
        binding.singInButton.isEnabled = false
        binding.emailTied.addTextChangedListener{
            validateFields()
        }
        binding.passwordTied.addTextChangedListener{
            validateFields()
        }
    }

    private fun validateFields{
        val email = binding.emailTied.text.toString().trim()
        val password = binding.passwordTied.text.toString().trim()

        val idEmailValid = isValidemail(email)
        val idpassword = password.length >= 8

        binding.emailTil.error = if (emial.isNotEmpty() && isEmailValid) null else "correo invalido"
        binding.passwordTil.error = if (password.isNotEmpty() && isEmailValid) null else "Minimo 8 caracteres"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister = view.findViewById<Button>(R.id.tvRegisterLink)
        val btnForgot = view.findViewById<Button>(R.id.tvForgotPass)

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}