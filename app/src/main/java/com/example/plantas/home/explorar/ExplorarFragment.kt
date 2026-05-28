package com.example.plantas.home.explorar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.plantas.core.FragmentCommunicator
import com.example.plantas.core.ResponseService
import com.example.plantas.databinding.FragmentExplorarBinding
import kotlinx.coroutines.launch

class ExplorarFragment : Fragment() {
    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlantasViewModel>()

    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        observerState()
        viewModel.loadPlantas()
        return binding.root
    }

    private fun observerState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.PlantaState.collect { state ->
                    state?.let {
                        when (it) {
                            is ResponseService.Loading -> {
                                communicator.manageLoader(true)
                            }
                            is ResponseService.Success -> {
                                Log.i("plantas", "plantas nuevas: ${it.data}")
                                communicator.manageLoader(false)
                            }
                            is ResponseService.Error -> {
                                communicator.manageLoader(false)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}