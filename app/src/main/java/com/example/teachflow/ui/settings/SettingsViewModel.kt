package com.example.teachflow.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
    
    fun toggleTheme() {
        viewModelScope.launch {
            _isDarkTheme.value = !_isDarkTheme.value
        }
    }
    
    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            _isDarkTheme.value = isDark
        }
    }
}
