package com.example.plantas.home.explorar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager // Cambiado
import androidx.recyclerview.widget.RecyclerView
import com.example.plantas.R
import com.example.plantas.core.FragmentCommunicator
import com.example.plantas.databinding.FragmentExplorarBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExplorarFragment : Fragment() {

    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantasAdapter: PlantasAdapter
    private val viewModel: PlantasViewModel by viewModels()

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupSearchView()

        viewModel.fetchPlantas()
    }

    private fun setupSearchView() {
        binding.searchViewPlantas.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchJob?.cancel()
                ejecutarBusqueda(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500)
                    if (newText.isNullOrEmpty()) {
                        viewModel.fetchPlantas(reiniciar = true)
                    } else {
                        ejecutarBusqueda(newText)
                    }
                }
                return false
            }
        })
    }

    private fun ejecutarBusqueda(query: String?) {
        if (!query.isNullOrEmpty()) {
            plantasAdapter.updateList(emptyList())
            viewModel.buscarPlanta(query)
        }
    }

    private fun setupRecyclerView() {
        plantasAdapter = PlantasAdapter(emptyList()) { plantaSeleccionada ->
            val previousEntry = findNavController().previousBackStackEntry
            val esSeleccionManual = previousEntry?.destination?.id == R.id.agregarManualFragment

            if (esSeleccionManual) {
                previousEntry?.savedStateHandle?.set("planta_seleccionada", plantaSeleccionada)
                findNavController().popBackStack()
            } else {
                val bundle = Bundle().apply {
                    putParcelable("planta", plantaSeleccionada)
                }
                findNavController().navigate(R.id.action_explorarFragment_to_detallesPlantaFragment, bundle)
            }
        }

        binding.rvPlantas.adapter = plantasAdapter

        binding.rvPlantas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // CORRECCIÓN AQUÍ: Casteamos correctamente a GridLayoutManager para evitar el crash
                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager

                    layoutManager?.let {
                        val visibleItemCount = it.childCount
                        val totalItemCount = it.itemCount
                        val pastVisibleItems = it.findFirstVisibleItemPosition()

                        if (!viewModel.isCargandoMas) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 5) {
                                viewModel.fetchPlantas()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.plantas.observe(viewLifecycleOwner) { listaReal ->
            plantasAdapter.updateList(listaReal)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isVisible ->
            (requireActivity() as? FragmentCommunicator)?.manageLoader(isVisible)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}