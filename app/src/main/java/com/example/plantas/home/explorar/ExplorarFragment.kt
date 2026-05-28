package com.example.plantas.home.explorar

import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        inflater: LayoutInflater, container, viewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentExplorarBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        return binding.root
    }

    fun observerState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifescycle.State.STARTED){
                viewModel.plantaState.collect {state ->
                    when(state){
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success ->{
                            Log.i("plantas", "plantas nuevas: ${state.data}")
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