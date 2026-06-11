package com.example.plantas.home.misPlantas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.plantas.R
import com.example.plantas.core.database.AppDatabase
import com.example.plantas.core.database.PlantaFavorita
import com.example.plantas.core.model.Planta
import com.example.plantas.databinding.FragmentAgregarManualBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AgregarManualFragment : Fragment() {

    private var _binding: FragmentAgregarManualBinding? = null
    private val binding get() = _binding!!
    private var rutaFotoSeleccionada: String = ""

    private val selectorDeImagen = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            rutaFotoSeleccionada = guardarImagenInternamente(it)
            Glide.with(this).load(rutaFotoSeleccionada).centerCrop().into(binding.imgFotoManual)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAgregarManualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Escuchar si regresamos de "Explorar" con una planta seleccionada
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Planta>("planta_seleccionada")
            ?.observe(viewLifecycleOwner) { planta ->
                binding.etEspecie.setText(planta.commonName)
                binding.etRiego.setText(planta.watering ?: "No especificado")
                binding.etLuz.setText(planta.sunlight?.joinToString(", ") ?: "No especificado")

                // Si la planta tiene foto en la API, podríamos cargarla aquí también
                planta.defaultImage?.regularUrl?.let { url ->
                    Glide.with(this).load(url).centerCrop().into(binding.imgFotoManual)
                    // Nota: Aquí podrías descargar esa imagen y guardarla internamente si quisieras
                }
            }

        binding.imgFotoManual.setOnClickListener { selectorDeImagen.launch("image/*") }

        // 2. Botón para ir a buscar a Explorar
        binding.btnBuscarPlanta.setOnClickListener {
            findNavController().navigate(R.id.action_agregarManualFragment_to_explorarFragment)
        }

        // 3. Guardado final
        binding.btnGuardarManual.setOnClickListener {
            val apodo = binding.etApodo.text.toString()
            val especie = binding.etEspecie.text.toString()
            val riego = binding.etRiego.text.toString()
            val luz = binding.etLuz.text.toString()

            if (apodo.isNotEmpty() && especie.isNotEmpty()) {
                guardarPlanta(apodo, especie, riego, luz)
            } else {
                Toast.makeText(context, "El apodo y la especie son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarPlanta(apodo: String, especie: String, riego: String, luz: String) {
        val nuevaPlanta = PlantaFavorita(
            idApi = 0,
            nombreOriginal = especie,
            apodo = apodo,
            imagenLocalUri = rutaFotoSeleccionada,
            riego = riego,
            sol = luz,
            tipo = "Manual",
            cuidados = "Sin cuidados específicos"
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            db.plantaDao().insertFavorita(nuevaPlanta)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "¡Planta guardada!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun guardarImagenInternamente(uri: android.net.Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().filesDir, "manual_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        return file.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}