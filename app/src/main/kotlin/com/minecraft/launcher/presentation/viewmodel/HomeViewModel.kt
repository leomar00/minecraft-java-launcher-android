package com.minecraft.launcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.usecase.AuthUseCase
import com.minecraft.launcher.domain.usecase.GameUseCase
import com.minecraft.launcher.domain.usecase.VersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authUseCase: AuthUseCase,
    private val versionUseCase: VersionUseCase,
    private val gameUseCase: GameUseCase
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    private val _installedVersions = MutableStateFlow<List<MinecraftVersion>>(emptyList())
    val installedVersions: StateFlow<List<MinecraftVersion>> = _installedVersions

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _totalGameSize = MutableStateFlow(0L)
    val totalGameSize: StateFlow<Long> = _totalGameSize

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try {
                // Check if user is logged in
                val accountsResult = authUseCase.getAccounts()
                _isLoggedIn.value = accountsResult is Result.Success &&
                        (accountsResult as Result.Success).data.isNotEmpty()

                // Load installed versions
                val versionsResult = versionUseCase.getInstalledVersions()
                when (versionsResult) {
                    is Result.Success -> {
                        _installedVersions.value = versionsResult.data
                        // Calculate total size
                        val totalSize = versionsResult.data.sumOf { it.size }
                        _totalGameSize.value = totalSize
                        _homeState.value = HomeState.Success
                    }
                    is Result.Error -> {
                        _homeState.value = HomeState.Error(versionsResult.message)
                    }
                    is Result.Loading -> {}
                }
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun launchGame() {
        viewModelScope.launch {
            _homeState.value = HomeState.Launching
            // TODO: Get latest profile and launch
        }
    }

    fun refreshData() {
        loadHomeData()
    }

    sealed class HomeState {
        object Loading : HomeState()
        object Success : HomeState()
        object Launching : HomeState()
        data class Error(val message: String) : HomeState()
    }
}
