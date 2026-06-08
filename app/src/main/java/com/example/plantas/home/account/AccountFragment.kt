package com.example.plantas.home.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.plantas.databinding.FragmentAccountBinding
import com.example.plantas.onboarding.MainActivity

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AccountViewModel by viewModels()

    // Selector de fotos moderno
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            Glide.with(this).load(it).circleCrop().into(binding.imgProfile)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mostrar email desde Firebase Auth
        binding.txtEmail.text = viewModel.getUserEmail()

        // 2. Cargar los datos del perfil desde Firestore
        viewModel.getUserProfile { profile ->
            profile?.let {
                // Asignamos nombre y apellido por separado como querías
                binding.txtName.text = it.firstName
                binding.txtLastName.text = it.lastName

                binding.txtPhone.text = "Tel: ${it.phone}"
                binding.txtFavoritePlant.text = "Planta favorita: ${it.favoritePlant}"
            }
        }

        // 3. Listeners para cambiar foto
        val imageClickListener = View.OnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.imgProfile.setOnClickListener(imageClickListener)
        // Asegúrate de que btnEditPhoto exista en tu XML (es el MaterialCardView que pusimos antes)
        binding.btnEditPhoto.setOnClickListener(imageClickListener)

        // 4. Listener para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            viewModel.signOut {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}