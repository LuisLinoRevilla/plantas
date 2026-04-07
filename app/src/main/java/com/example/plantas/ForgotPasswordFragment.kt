package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class ForgotPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño de recuperar contraseña
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Buscamos el ícono de la flecha
        val ivBack = view.findViewById<ImageView>(R.id.ivBackForgot)

        // 2. Le decimos qué hacer al tocarlo
        ivBack.setOnClickListener {
            // Este comando te regresa a la pantalla anterior automáticamente
            findNavController().navigateUp()
        }
    }
}