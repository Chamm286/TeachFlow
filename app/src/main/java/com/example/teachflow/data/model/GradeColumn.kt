package com.example.teachflow.data.model

data class GradeColumn(
    val id: String = "",
    val classId: String = "",
    val columnName: String = "",
    val columnType: String = "",
    val weight: Int = 1,
    val displayOrder: Int = 0,
    val maxScore: Int = 10
)