package com.example.teachflow.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teachflow.data.model.GradeTable
import com.example.teachflow.data.model.StudentWithScores
import com.example.teachflow.data.model.GradeColumn

@Composable
fun GradeTable(
    gradeTable: GradeTable,
    onScoreChange: (String, String, Float?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "H?c sinh",
                modifier = Modifier.weight(2f),
                style = MaterialTheme.typography.titleSmall
            )
            gradeTable.gradeColumns.forEach { column ->
                Text(
                    text = column.columnName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Divider()

        LazyColumn {
            items(gradeTable.students) { student ->
                StudentGradeRow(
                    student = student,
                    columns = gradeTable.gradeColumns,
                    onScoreChange = onScoreChange
                )
                Divider()
            }
        }
    }
}

@Composable
fun StudentGradeRow(
    student: StudentWithScores,
    columns: List<GradeColumn>,
    onScoreChange: (String, String, Float?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = student.student.fullName,
            modifier = Modifier.weight(2f)
        )

        columns.forEach { column ->
            var scoreText by remember {
                mutableStateOf(student.scores[column.id]?.toString() ?: "")
            }

            OutlinedTextField(
                value = scoreText,
                onValueChange = {
                    scoreText = it
                    val value = it.toFloatOrNull()
                    onScoreChange(student.student.id, column.id, value)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                placeholder = { Text("-") },
                singleLine = true
            )
        }
    }
}
