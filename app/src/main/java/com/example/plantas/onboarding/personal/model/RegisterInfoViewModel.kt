package com.example.plantas.onboarding.personal.model

import com.example.plantas.core.repositories.UserRepository
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterInfoViewModel {
    private val repository = UserRepository()

    private val _saveState = MutableStateFlow<Response<Unit>?>(null
    val saveState: StateFlow<ResponseService>
}
