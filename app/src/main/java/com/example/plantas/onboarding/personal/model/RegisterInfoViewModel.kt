package com.example.plantas.onboarding.personal.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.core.ResponseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterInfoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _saveState = MutableStateFlow<ResponseService<Unit>?>(null)
    val saveState: StateFlow<ResponseService<Unit>?> = _saveState.asStateFlow()


    fun guardarDatosUsuario(firstName: String, lastName: String, phone: String, favoritePlant: String) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            _saveState.value = ResponseService.Error("Usuario no autenticado")
            return
        }


        val userProfile = UserProfile(
            id = uid,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            favoritePlant = favoritePlant
        )

        viewModelScope.launch {
            _saveState.value = ResponseService.Loading
            try {
                // Guardamos en Firestore en la colección "users"
                db.collection("users").document(uid).set(userProfile).await()

                _saveState.value = ResponseService.Success(data = Unit)
            } catch (e: Exception) {
                _saveState.value = ResponseService.Error(e.localizedMessage ?: "Error al guardar perfil")
            }
        }
    }
}