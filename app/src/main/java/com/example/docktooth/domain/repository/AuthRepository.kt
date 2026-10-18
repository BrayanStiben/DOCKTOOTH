package com.example.docktooth.domain.repository

import com.example.docktooth.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User>
    fun login(emailOrUser: String, password: String): Boolean
    fun register(user: User): Boolean
    fun logout()
}
