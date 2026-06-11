package com.example.plantas.home.misPlantas

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.plantas.R
import com.example.plantas.core.database.AppDatabase
import com.example.plantas.core.database.PlantaFavorita
import com.example.plantas.databinding.FragmentDetallesPlantaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DetalleFavoritaFragment : Fragment() {

    private var _binding: FragmentDetallesPlantaBinding? = null
    private val binding get() = _binding!!

    private var plantaActual: PlantaFavorita? = null

    // El "contrato" para abrir la galería
    private val selectorDeImagen = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uriTemporal ->
        if (uriTemporal != null) {
            val rutaSegura = guardarImagenInternamente(uriTemporal)

            Glide.with(this)
                .load(rutaSegura)
                .centerCrop()
                .placeholder(R.drawable.icono_flor)
                .error(R.drawable.icono_flor)
                .into(binding.imgFotoDetalle)

            plantaActual?.let { planta ->
                planta.imagenLocalUri = rutaSegura
                actualizarPlantaEnBD(planta)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesPlantaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Control visual de los botones flotantes (FAB)
        binding.fabGuardarFavorita.visibility = View.GONE
        binding.fabBorrarFavorita.visibility = View.VISIBLE

        val plantaGuardada = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("planta_favorita", PlantaFavorita::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("planta_favorita")
        }

        plantaGuardada?.let { planta ->
            plantaActual = planta // Guardamos la referencia para el selector de imagen

            binding.txtNombreComunDetalle.text = planta.apodo
            binding.txtNombreCientificoDetalle.text = planta.nombreOriginal
            binding.txtRiegoDetalle.text = planta.riego
            binding.txtSolDetalle.text = planta.sol
            binding.txtTipoDetalle.text = planta.tipo
            binding.txtCuidadosDetalle.text = planta.cuidados

            // CORRECCIÓN: Validación para plantas manuales (como Pepito) para evitar el hueco en blanco
            val recomendaciones = if (!planta.cuidadosEspecificos.isNullOrEmpty() && planta.cuidadosEspecificos != "Cargando recomendaciones...") {
                planta.cuidadosEspecificos
            } else {
                "Para tu planta personalizada, te recomendamos mantener un monitoreo constante de la humedad de su tierra (idealmente usando sustratos aireados) y asegurar que reciba iluminación acorde a su tipo."
            }
            binding.txtCuidadosEspecificosDetalle.text = recomendaciones

            // Carga segura de la imagen con placeholders de respaldo
            Glide.with(this)
                .load(planta.imagenLocalUri)
                .centerCrop()
                .placeholder(R.drawable.icono_flor)
                .error(R.drawable.icono_flor)
                .into(binding.imgFotoDetalle)

            // Clic para cambiar foto desde la galería
            binding.imgFotoDetalle.setOnClickListener {
                selectorDeImagen.launch("image/*")
            }

            setupBotonBorrar(planta)
        }
    }

    private fun setupBotonBorrar(planta: PlantaFavorita) {
        binding.fabBorrarFavorita.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(requireContext())
                db.plantaDao().deleteFavorita(planta)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "${planta.apodo} eliminada", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun guardarImagenInternamente(uri: android.net.Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val archivoImagen = File(requireContext().filesDir, "planta_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(archivoImagen)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return archivoImagen.absolutePath
    }

    private fun actualizarPlantaEnBD(plantaActualizada: PlantaFavorita) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            db.plantaDao().updateFavorita(plantaActualizada)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "¡Foto actualizada!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}