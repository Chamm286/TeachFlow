package com.example.teachflow.data.repository

import com.example.teachflow.data.model.Class
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow

class ClassRepository(private val firebaseService: FirebaseService) {

    fun getClassesByTeacher(teacherId: String): Flow<List<Class>> {
        return firebaseService.getClassesByTeacher(teacherId)
    }
}