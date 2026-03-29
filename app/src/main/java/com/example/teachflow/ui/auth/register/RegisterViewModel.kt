package com.example.teachflow.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.launch
import android.util.Log

class RegisterViewModel : ViewModel() {
    private val firebaseService = FirebaseService()

    fun register(
        name: String, 
        email: String, 
        password: String, 
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            Log.d("REGISTER_DEBUG", "📝 Đang đăng ký với email: , role: ")
            
            try {
                // Sửa đúng thứ tự tham số: email, password, fullName, role
                val user = firebaseService.register(
                    email = email,
                    password = password,
                    fullName = name,
                    role = role,
                    studentCode = "",
                    className = ""
                )
                
                if (user != null) {
                    Log.d("REGISTER_DEBUG", "✅ Đăng ký thành công với email: ")
                    onResult(true, null)
                } else {
                    Log.e("REGISTER_DEBUG", "❌ Đăng ký thất bại - user null")
                    onResult(false, "Đăng ký thất bại, email có thể đã tồn tại")
                }
            } catch (e: Exception) {
                Log.e("REGISTER_DEBUG", "❌ Lỗi đăng ký: ")
                onResult(false, "Có lỗi xảy ra: ")
            }
        }
    }
}
