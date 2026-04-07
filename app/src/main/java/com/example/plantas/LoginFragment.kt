package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Aquí se dibuja tu diseño XML
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    // AQUÍ AGREGAREMOS LA LÓGICA DE LOS BOTONES
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Buscamos los botones que pusimos en el XML usando sus IDs
        val btnRegister = view.findViewById<Button>(R.id.tvRegisterLink)
        val btnForgot = view.findViewById<Button>(R.id.tvForgotPass)

        // 2. Acción para el botón de Registrarse
        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // 3. Acción para el botón de Restablecer contraseña
        btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}