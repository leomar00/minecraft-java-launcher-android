package com.minecraft.launcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.domain.model.Result
import com.minecraft.launcher.domain.model.VersionManifest
import com.minecraft.launcher.domain.usecase.VersionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VersionsViewModel(private val versionUseCase: VersionUseCase) : ViewModel() {
    private val _versionsState = MutableStateFlow<VersionsState>(VersionsState.Idle)
    val versionsState: StateFlow<VersionsState> = _versionsState

    private val _availableVersions = MutableStateFlow<List<MinecraftVersion>>(emptyList())
    val availableVersions: StateFlow<List<MinecraftVersion>> = _availableVersions

    private val _installedVersions = MutableStateFlow<List<MinecraftVersion>>(emptyList())
    val installedVersions: StateFlow<List<MinecraftVersion>> = _installedVersions

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress

    private val _latestRelease = MutableStateFlow<String?>(null)
    val latestRelease: StateFlow<String?> = _latestRelease

    fun loadVersions() {
        viewModelScope.launch {
            _versionsState.value = VersionsState.Loading
            try {
                val result = versionUseCase.getAvailableVersions()
                when (result) {
                    is Result.Success -> {
                        _availableVersions.value = result.data.versions
                        _latestRelease.value = result.data.latest.release
                        loadInstalledVersions()
                    }
                    is Result.Error -> {
                        _versionsState.value = VersionsState.Error(result.message)
                    }
                    is Result.Loading -> {}
                }
            } catch (e: Exception) {
                _versionsState.value = VersionsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun loadInstalledVersions() {
        viewModelScope.launch {
            val result = versionUseCase.getInstalledVersions()
            when (result) {
                is Result.Success -> {
                    _installedVersions.value = result.data
                    _versionsState.value = VersionsState.Success
                }
                is Result.Error -> {
                    _versionsState.value = VersionsState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun installVersion(version: MinecraftVersion) {
        viewModelScope.launch {
            _versionsState.value = VersionsState.Installing
            val result = versionUseCase.installVersion(version) { progress ->
                _downloadProgress.value = progress
            }
            when (result) {
                is Result.Success -> {
                    loadInstalledVersions()
                    _versionsState.value = VersionsState.Success
                }
                is Result.Error -> {
                    _versionsState.value = VersionsState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun uninstallVersion(versionId: String) {
        viewModelScope.launch {
            _versionsState.value = VersionsState.Loading
            val result = versionUseCase.uninstallVersion(versionId)
            when (result) {
                is Result.Success -> {
                    loadInstalledVersions()
                }
                is Result.Error -> {
                    _versionsState.value = VersionsState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    sealed class VersionsState {
        object Idle : VersionsState()
        object Loading : VersionsState()
        object Success : VersionsState()
        object Installing : VersionsState()
        data class Error(val message: String) : VersionsState()
    }
}
