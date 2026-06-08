package com.example.plantas.home.misPlantas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantas.R
import com.example.plantas.core.database.AppDatabase
import com.example.plantas.databinding.FragmentMisPlantasBinding
import kotlinx.coroutines.launch

class MisPlantasFragment : Fragment() {

    private var _binding: FragmentMisPlantasBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MisPlantasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisPlantasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observarBaseDeDatos()

        // Conexión final: Navegación al formulario de planta manual
        binding.fabAgregarManual.setOnClickListener {
            findNavController().navigate(R.id.action_misPlantasFragment_to_agregarManualFragment)
        }
    }

    private fun setupRecyclerView() {
        // Inicializamos con lista vacía y configuramos el clic para ver detalles
        adapter = MisPlantasAdapter(emptyList()) { plantaSeleccionada ->
            val bundle = Bundle().apply {
                putParcelable("planta_favorita", plantaSeleccionada)
            }
            findNavController().navigate(R.id.action_misPlantasFragment_to_detalleFavoritaFragment, bundle)
        }

        binding.recyclerMisPlantas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMisPlantas.adapter = adapter
    }

    private fun observarBaseDeDatos() {
        // Recolectamos los datos de Room de forma reactiva
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val db = AppDatabase.getDatabase(requireContext())

                db.plantaDao().getAllFavoritas().collect { listaFavoritas ->
                    // 1. Actualizamos el adaptador con los datos nuevos
                    adapter.actualizarLista(listaFavoritas)

                    // CORRECCIÓN AQUÍ: Ocultamos o mostramos TODO el contenedor (Flor + Texto)
                    binding.layoutListaVacia.isVisible = listaFavoritas.isEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}