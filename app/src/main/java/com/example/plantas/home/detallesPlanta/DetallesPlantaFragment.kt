package com.example.plantas.home.detallesPlanta

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.plantas.core.FragmentCommunicator
import com.example.plantas.core.database.AppDatabase
import com.example.plantas.core.database.PlantaFavorita
import com.example.plantas.core.model.Planta
import com.example.plantas.databinding.FragmentDetallesPlantaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetallesPlantaFragment : Fragment() {

    private var _binding: FragmentDetallesPlantaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetallesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesPlantaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantaBase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("planta", Planta::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("planta")
        }

        plantaBase?.let { planta ->
            // Pintamos todo inmediatamente usando el objeto que ya trae los datos reales de la lista
            binding.txtNombreComunDetalle.text = planta.commonName?.replaceFirstChar { it.uppercase() } ?: "Nombre desconocido"
            binding.txtNombreCientificoDetalle.text = planta.scientificName?.firstOrNull() ?: "Sin datos científicos"

            binding.txtRiegoDetalle.text = planta.watering?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtSolDetalle.text = planta.sunlight?.joinToString(", ")?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtTipoDetalle.text = planta.cycle?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtCuidadosDetalle.text = planta.careLevel?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtCuidadosEspecificosDetalle.text = planta.wateringGeneral ?: "No hay recomendaciones específicas."

            // CORRECCIÓN DE IMAGEN: Usamos regularUrl que es la que tiene el JSON de las 30
            val urlImagen = planta.defaultImage?.regularUrl ?: planta.defaultImage?.originalUrl
            Glide.with(this)
                .load(urlImagen)
                .centerCrop()
                .placeholder(com.example.plantas.R.drawable.icono_flor) // Cambia por tu drawable de respaldo
                .error(com.example.plantas.R.drawable.icono_flor)
                .into(binding.imgFotoDetalle)

            setupFabGuardar(planta)
        }

    }

    private fun setupObservers() {
        viewModel.plantaDetalle.observe(viewLifecycleOwner) { plantaCompleta ->
            binding.txtRiegoDetalle.text = plantaCompleta.watering?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtSolDetalle.text = plantaCompleta.sunlight?.joinToString(", ")?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtTipoDetalle.text = plantaCompleta.cycle?.replaceFirstChar { it.uppercase() } ?: "No disponible"
            binding.txtCuidadosDetalle.text = plantaCompleta.careLevel?.replaceFirstChar { it.uppercase() } ?: "No disponible"

            // Asignamos la recomendación específica del JSON
            binding.txtCuidadosEspecificosDetalle.text = plantaCompleta.wateringGeneral ?: "No hay recomendaciones específicas."
        }
    }

    private fun setupFabGuardar(plantaRecibida: Planta) {
        binding.fabGuardarFavorita.setOnClickListener {
            mostrarDialogoGuardar(plantaRecibida)
        }
    }

    private fun mostrarDialogoGuardar(planta: Planta) {
        val input = EditText(requireContext())
        input.hint = "Ej. Plantita de mi escritorio"

        AlertDialog.Builder(requireContext())
            .setTitle("Guardar en Mis Plantas")
            .setMessage("¿Qué apodo le pondrás a tu ${planta.commonName ?: "planta"}?")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val apodoIngresado = input.text.toString().trim()
                val apodoFinal = if (apodoIngresado.isNotEmpty()) apodoIngresado else (planta.commonName ?: "Mi Planta")

                guardarEnBaseDeDatos(planta, apodoFinal)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarEnBaseDeDatos(planta: Planta, apodo: String) {
        val urlImagen = planta.defaultImage?.regularUrl ?: planta.defaultImage?.thumbnail ?: ""

        val riegoActual = binding.txtRiegoDetalle.text.toString()
        val solActual = binding.txtSolDetalle.text.toString()
        val tipoActual = binding.txtTipoDetalle.text.toString()
        val cuidadosActual = binding.txtCuidadosDetalle.text.toString()
        val especificaActual = binding.txtCuidadosEspecificosDetalle.text.toString() // Nuevo

        val favorita = PlantaFavorita(
            idApi = planta.id ?: 0,
            nombreOriginal = planta.commonName ?: "Desconocido",
            apodo = apodo,
            imagenLocalUri = urlImagen,
            riego = riegoActual,
            sol = solActual,
            tipo = tipoActual,
            cuidados = cuidadosActual,
            cuidadosEspecificos = especificaActual // Guardado en Room
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                db.plantaDao().insertFavorita(favorita)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "¡$apodo guardada con éxito!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}