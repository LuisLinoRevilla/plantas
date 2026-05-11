package com.example.plantas.onboarding.personal.model

import androidx.lifecycle.ViewModel
import com.example.plantas.core.repositories.UserRepository
import com.example.plantas.core.ResponseService // <-- 1. Tu propio ResponseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterInfoViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _saveState = MutableStateFlow<ResponseService<Unit>?>(null)

    val saveState: StateFlow<ResponseService<Unit>?> = _saveState.asStateFlow()
}