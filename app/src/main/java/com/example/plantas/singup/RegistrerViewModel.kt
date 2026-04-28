package com.example.plantas.singup

import androidx.lifecycle.ViewModel
import com.example.plantas.core.AuthRepository
import com.example.plantas.core.ResponseService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel: ViewModel() {
    private val authRepository = AuthRepository()

    @Suppress("UNUSED_PARAMETER")
    private val _registerState = MutableStateFlow<ResponseService<FirebaseUser>>(value = null)
    val registerState = _registerState.asStateFlow()
}