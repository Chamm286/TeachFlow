package com.example.teachflow.ui.teacher.grade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.GradeTable
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GradeInputViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    private val _uiState = MutableStateFlow(GradeInputUiState())
    val uiState: StateFlow<GradeInputUiState> = _uiState
    
    private val pendingChanges = mutableMapOf<String, Float?>()

    fun loadGradeTable(classId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val gradeTable = firebaseService.getGradeTable(classId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                gradeTable = gradeTable
            )
        }
    }

    fun updateScore(studentId: String, columnId: String, value: Float?) {
        val key = "_"
        pendingChanges[key] = value
    }

    fun saveGrades() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            for ((key, value) in pendingChanges) {
                val parts = key.split("_")
                if (parts.size == 2) {
                    firebaseService.updateScore(parts[0], parts[1], value)
                }
            }
            
            pendingChanges.clear()
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            // Reload data
            _uiState.value.gradeTable?.classInfo?.id?.let { loadGradeTable(it) }
        }
    }
}

data class GradeInputUiState(
    val isLoading: Boolean = false,
    val gradeTable: GradeTable? = null,
    val error: String? = null
)
