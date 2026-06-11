package com.example.plantas.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.core.AuthRepository
import com.example.plantas.onboarding.personal.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val auth = FirebaseAuth.getInstance()

    // Suponiendo que usas Firestore para guardar el perfil
    private val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    fun getUserEmail(): String = auth.currentUser?.email ?: "usuario@ejemplo.com"

    // Nueva función para traer los datos del perfil
    fun getUserProfile(onResult: (UserProfile?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val profile = document.toObject(UserProfile::class.java)
                onResult(profile)
            }
            .addOnFailureListener { onResult(null) }
    }

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.signOut()
            onSuccess()
        }
    }
}