package com.example.teachflow.ui.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ForgotPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun sendResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // TODO: Implement Firebase Auth password reset
            Log.d("ForgotPassword", "Send reset email to: ")
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }

    fun resetState() {
        _uiState.value = ForgotPasswordUiState()
    }
}

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
