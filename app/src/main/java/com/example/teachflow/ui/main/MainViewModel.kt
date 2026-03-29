package com.example.teachflow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.remote.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class MainViewModel : ViewModel() {
    private val firebaseService = FirebaseService()
    
    private val _stats = MutableStateFlow(StatsData())
    val stats: StateFlow<StatsData> = _stats
    
    private val _articles = MutableStateFlow<List<ArticleData>>(emptyList())
    val articles: StateFlow<List<ArticleData>> = _articles
    
    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount
    
    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            try {
                // Lấy thống kê từ FirebaseService
                // Note: FirebaseService không có method getUsers/getClasses trực tiếp
                // Nên dùng tạm dữ liệu mẫu + lấy thông tin user hiện tại
                
                _stats.value = StatsData(
                    totalUsers = 1250,
                    totalClasses = 48,
                    rating = 4.9
                )
                
                // Lấy bài viết mẫu (có thể thay bằng API sau)
                _articles.value = getMockArticles()
                
                // Lấy số thông báo chưa đọc
                _notificationCount.value = 3
                
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading data: ")
                _stats.value = StatsData(1250, 48, 4.9)
                _articles.value = getMockArticles()
                _notificationCount.value = 3
            }
        }
    }
    
    private fun getMockArticles(): List<ArticleData> {
        return listOf(
            ArticleData("1", "Cách quản lý lớp học hiệu quả", "Những bí quyết giúp giáo viên quản lý lớp học tốt hơn...", "Hôm qua", "5 phút đọc"),
            ArticleData("2", "Xu hướng giáo dục 2024", "Công nghệ AI đang thay đổi giáo dục như thế nào...", "2 ngày trước", "8 phút đọc"),
            ArticleData("3", "Phương pháp học tập thông minh", "Kỹ thuật Pomodoro và spaced repetition...", "5 ngày trước", "6 phút đọc")
        )
    }
}

data class StatsData(
    val totalUsers: Int = 0,
    val totalClasses: Int = 0,
    val rating: Double = 0.0
)

data class ArticleData(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val readTime: String = ""
)

data class NotificationData(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val isRead: Boolean = false
)
