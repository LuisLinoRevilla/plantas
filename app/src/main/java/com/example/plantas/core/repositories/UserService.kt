package com.example.plantas.core.repositories

import com.example.plantas.core.ResponseService
import com.example.plantas.onboarding.personal.model.UserProfile

interface UserService {
    suspend fun seveUserInfo(userProfile: UserProfile): ResponseService<Unit>
}