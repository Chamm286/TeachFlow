package com.example.teachflow.ui.teacher.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.Class
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassesViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(ClassesUiState())
    val uiState: StateFlow<ClassesUiState> = _uiState

    fun loadClasses(teacherId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            firebaseService.getClassesByTeacher(teacherId).collect { classes ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    classes = classes
                )
            }
        }
    }
}

data class ClassesUiState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val error: String? = null
)
