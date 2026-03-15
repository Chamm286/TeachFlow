package com.example.teachflow.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.User
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class RegisterViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(
        email: String,
        password: String,
        fullName: String,
        role: String,
        studentCode: String = "",
        className: String = ""
    ) {
        viewModelScope.launch {
            Log.d("REGISTER_DEBUG", "📝 Đang đăng ký với email: $email")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Kiểm tra mật khẩu xác nhận
            // (Việc này nên làm ở UI nhưng check lại ở đây)

            val user = firebaseService.register(
                email = email,
                password = password,
                fullName = fullName,
                role = role,
                studentCode = studentCode,
                className = className
            )

            if (user != null) {
                Log.d("REGISTER_DEBUG", "✅ Đăng ký thành công: ${user.id} - ${user.role}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    user = user
                )
            } else {
                Log.e("REGISTER_DEBUG", "❌ Đăng ký thất bại - email đã tồn tại")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Email đã tồn tại"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)