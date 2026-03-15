package com.example.teachflow.data.repository

import com.example.teachflow.data.model.GradeTable
import com.example.teachflow.data.model.Score
import com.example.teachflow.data.remote.FirebaseService

class GradeRepository(private val firebaseService: FirebaseService) {

    suspend fun getGradeTable(classId: String): GradeTable? {
        return firebaseService.getGradeTable(classId)
    }

    suspend fun updateScore(studentId: String, columnId: String, value: Float?) {
        firebaseService.updateScore(studentId, columnId, value)
    }
}