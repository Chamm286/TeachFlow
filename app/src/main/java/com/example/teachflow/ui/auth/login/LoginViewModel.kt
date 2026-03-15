package com.example.teachflow.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.User
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class LoginViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LOGIN_DEBUG", "🔑 Đang đăng nhập với email: $email")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val user = firebaseService.login(email, password)
            Log.d("LOGIN_DEBUG", "📦 User từ Firebase: $user")

            if (user != null) {
                Log.d("LOGIN_DEBUG", "✅ Đăng nhập thành công: ${user.id} - ${user.role}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    user = user
                )
            } else {
                Log.e("LOGIN_DEBUG", "❌ Đăng nhập thất bại - user null")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Email không tồn tại"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)