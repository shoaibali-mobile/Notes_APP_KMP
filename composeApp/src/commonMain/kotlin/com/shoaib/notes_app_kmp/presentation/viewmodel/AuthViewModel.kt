package com.shoaib.notes_app_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoaib.notes_app_kmp.data.local.UserEntity
import com.shoaib.notes_app_kmp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(
        username: String,
        password: String,
        onSuccess: (UserEntity) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authRepository.login(username, password)
                if (user != null) {
                    _currentUser.value = user
                    onSuccess(user)
                } else {
                    onError("Invalid username or password")
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signup(
        username: String,
        password: String,
        onSuccess: (UserEntity) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authRepository.signup(username, password)
                _currentUser.value = user
                onSuccess(user)
            } catch (e: Exception) {
                onError("Signup failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

}