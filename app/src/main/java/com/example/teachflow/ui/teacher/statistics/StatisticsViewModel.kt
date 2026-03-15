package com.example.teachflow.ui.teacher.statistics

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatisticsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState
}

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
