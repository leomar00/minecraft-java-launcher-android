package com.minecraft.launcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraft.launcher.data.local.preferences.AccountPreferences
import com.minecraft.launcher.domain.model.MinecraftAccount
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authUseCase: AuthUseCase,
    private val accountPreferences: AccountPreferences
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _accounts = MutableStateFlow<List<MinecraftAccount>>(emptyList())
    val accounts: StateFlow<List<MinecraftAccount>> = _accounts

    private val _currentAccount = MutableStateFlow<MinecraftAccount?>(null)
    val currentAccount: StateFlow<MinecraftAccount?> = _currentAccount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadCurrentAccount()
        loadAccounts()
    }

    fun loginOffline(username: String) {
        if (username.isBlank()) {
            _authState.value = AuthState.Error("Username cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
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
            _isLoading.value = false
        }
    }

    fun loginWithUsername(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            val result = authUseCase.loginWithUsername(username, password)
            when (result) {
                is Result.Success -> {
                    _currentAccount.value = result.data
                    _authState.value = AuthState.Success("Logged in successfully")
                    loadAccounts()
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun loginWithMicrosoft(code: String) {
        if (code.isBlank()) {
            _authState.value = AuthState.Error("Auth code cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
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
            _isLoading.value = false
        }
    }

    fun logout(accountId: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            val result = authUseCase.logout(accountId)
            when (result) {
                is Result.Success -> {
                    if (_currentAccount.value?.id == accountId) {
                        _currentAccount.value = null
                    }
                    _authState.value = AuthState.Success("Logged out")
                    loadAccounts()
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is Result.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun switchAccount(account: MinecraftAccount) {
        viewModelScope.launch {
            _currentAccount.value = account
            accountPreferences.saveCurrentAccount(account)
            _authState.value = AuthState.Success("Switched to ${account.username}")
        }
    }

    private fun loadCurrentAccount() {
        viewModelScope.launch {
            val account = accountPreferences.getCurrentAccount()
            _currentAccount.value = account
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

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
