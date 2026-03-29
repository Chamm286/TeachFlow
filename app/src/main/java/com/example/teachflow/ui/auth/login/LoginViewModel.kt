package com.example.teachflow.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.User
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.launch
import android.util.Log

class LoginViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _isSuccess = MutableLiveData(false)
    val isSuccess: LiveData<Boolean> = _isSuccess
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LOGIN_DEBUG", "Đang đăng nhập với email: ")
            _isLoading.value = true
            _error.value = null

            val user = firebaseService.login(email, password)
            Log.d("LOGIN_DEBUG", "User từ Firebase: ")

            if (user != null) {
                Log.d("LOGIN_DEBUG", "Đăng nhập thành công:  - ")
                _isLoading.value = false
                _isSuccess.value = true
                _user.value = user
            } else {
                Log.e("LOGIN_DEBUG", "Đăng nhập thất bại - user null")
                _isLoading.value = false
                _error.value = "Email hoặc mật khẩu không đúng"
            }
        }
    }

    fun resetState() {
        _isSuccess.value = false
        _error.value = null
        _user.value = null
    }
}
