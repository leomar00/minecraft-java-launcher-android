package com.minecraft.launcher.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _allocatedMemory = MutableStateFlow(2048)
    val allocatedMemory: StateFlow<Int> = _allocatedMemory

    private val _gameDirectory = MutableStateFlow("/storage/emulated/0/.minecraft")
    val gameDirectory: StateFlow<String> = _gameDirectory

    private val _vmArguments = MutableStateFlow("-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC")
    val vmArguments: StateFlow<String> = _vmArguments

    private val _javaPath = MutableStateFlow<String?>(null)
    val javaPath: StateFlow<String?> = _javaPath

    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val settingsState: StateFlow<SettingsState> = _settingsState

    fun updateAllocatedMemory(memory: Int) {
        viewModelScope.launch {
            _allocatedMemory.value = memory.coerceIn(512, 4096)
        }
    }

    fun updateGameDirectory(path: String) {
        viewModelScope.launch {
            _gameDirectory.value = path
        }
    }

    fun updateVMArguments(args: String) {
        viewModelScope.launch {
            _vmArguments.value = args
        }
    }

    fun updateJavaPath(path: String) {
        viewModelScope.launch {
            _javaPath.value = path
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _settingsState.value = SettingsState.Loading
            try {
                // TODO: Implement settings persistence
                _settingsState.value = SettingsState.Success("Settings saved")
            } catch (e: Exception) {
                _settingsState.value = SettingsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            _allocatedMemory.value = 2048
            _gameDirectory.value = "/storage/emulated/0/.minecraft"
            _vmArguments.value = "-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"
            _javaPath.value = null
            _settingsState.value = SettingsState.Success("Settings reset")
        }
    }

    sealed class SettingsState {
        object Idle : SettingsState()
        object Loading : SettingsState()
        data class Success(val message: String) : SettingsState()
        data class Error(val message: String) : SettingsState()
    }
}
