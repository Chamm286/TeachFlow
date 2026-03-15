package com.example.teachflow.ui.teacher.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeacherDashboardViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(TeacherDashboardUiState())
    val uiState: StateFlow<TeacherDashboardUiState> = _uiState

    fun loadData(teacherId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // TODO: Load data from Firebase
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}

data class TeacherDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
