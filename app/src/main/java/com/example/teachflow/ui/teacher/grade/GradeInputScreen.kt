package com.example.teachflow.ui.teacher.grade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teachflow.ui.components.LoadingIndicator
import com.example.teachflow.ui.components.GradeTable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeInputScreen(
    teacherId: String,
    classId: String = "",
    className: String = "",
    onBack: () -> Unit,
    viewModel: GradeInputViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(classId) {
        if (classId.isNotEmpty()) {
            viewModel.loadGradeTable(classId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (className.isNotEmpty()) "Nh?p di?m - " else "Nh?p di?m") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.saveGrades() },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Luu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                LoadingIndicator()
            } else if (uiState.gradeTable == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Chua có d? li?u",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {
                GradeTable(
                    gradeTable = uiState.gradeTable!!,
                    onScoreChange = { studentId, columnId, value ->
                        viewModel.updateScore(studentId, columnId, value)
                    }
                )
            }
        }
    }
}
