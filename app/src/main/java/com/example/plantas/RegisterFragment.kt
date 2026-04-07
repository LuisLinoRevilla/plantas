package com.example.plantas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño de la pantalla de registro
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Buscamos la flecha de regresar y el botón verde de registrar
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        // 2. Acción para la flecha: Destruye esta pantalla y regresa al Login
        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 3. Acción para el botón verde: Nos lleva a la pantalla de información adicional
        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerInfoFragment)
        }
    }
}