package com.minecraft.launcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _accounts = MutableStateFlow<List<MinecraftAccount>>(emptyList())
    val accounts: StateFlow<List<MinecraftAccount>> = _accounts

    private val _currentAccount = MutableStateFlow<MinecraftAccount?>(null)
    val currentAccount: StateFlow<MinecraftAccount?> = _currentAccount

    init {
        loadAccounts()
    }

    fun loginOffline(username: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authUseCase.loginOffline(username)
            when (result) {
                is Result.Success -> {
                    _currentAccount.value = result.data
                    _authState.value = AuthState.Success("Logged in as ${result.data.username}")
                    loadAccounts()
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun loginWithMicrosoft(code: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authUseCase.loginWithMicrosoft(code)
            when (result) {
                is Result.Success -> {
                    _currentAccount.value = result.data
                    _authState.value = AuthState.Success("Logged in with Microsoft")
                    loadAccounts()
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun logout(accountId: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authUseCase.logout(accountId)
            when (result) {
                is Result.Success -> {
                    _currentAccount.value = null
                    _authState.value = AuthState.Success("Logged out")
                    loadAccounts()
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            val result = authUseCase.getAccounts()
            when (result) {
                is Result.Success -> {
                    _accounts.value = result.data
                }
                is Result.Error -> {
                    // Handle error silently
                }
                is Result.Loading -> {}
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
