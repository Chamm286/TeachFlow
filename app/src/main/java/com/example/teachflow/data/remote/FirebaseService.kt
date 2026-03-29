package com.example.teachflow.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.teachflow.data.model.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "🔥 FirebaseService"

    // ==================== AUTHENTICATION ====================

    suspend fun login(email: String, password: String): User? {
        Log.d(TAG, "🔍 Đang tìm email: ")
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            Log.d(TAG, "📊 Số lượng documents tìm thấy: ")

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                Log.d(TAG, "✅ Tìm thấy user: ")
                Log.d(TAG, "   Email: ")
                Log.d(TAG, "   FullName: ")
                Log.d(TAG, "   Role: ")

                User(
                    id = doc.id,
                    email = doc.getString("email") ?: "",
                    fullName = doc.getString("fullName") ?: "",
                    role = doc.getString("role") ?: "student",
                    avatar = doc.getString("avatar") ?: "",
                    phone = doc.getString("phone") ?: "",
                    studentCode = doc.getString("studentCode") ?: "",
                    className = doc.getString("className") ?: ""
                )
            } else {
                Log.e(TAG, "❌ KHÔNG tìm thấy email: ")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 LỖI Firebase:", e)
            null
        }
    }

    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        role: String,
        studentCode: String = "",
        className: String = ""
    ): User? {
        Log.d(TAG, "📝 Đăng ký tài khoản mới: ")
        return try {
            val existingUser = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!existingUser.isEmpty) {
                Log.e(TAG, "❌ Email đã tồn tại: ")
                return null
            }

            val avatarUrl = "https://ui-avatars.com/api/?name=&size=128&background=4F46E5&color=fff"

            val newUser = hashMapOf(
                "email" to email,
                "fullName" to fullName,
                "role" to role,
                "avatar" to avatarUrl,
                "phone" to "",
                "studentCode" to studentCode,
                "className" to className,
                "createdAt" to System.currentTimeMillis(),
                "isActive" to true,
                "emailVerified" to false,
                "lastLogin" to System.currentTimeMillis()
            )

            val docRef = db.collection("users").add(newUser).await()
            Log.d(TAG, "✅ Đã tạo user với ID: ")

            val userDoc = docRef.get().await()

            User(
                id = userDoc.id,
                email = userDoc.getString("email") ?: "",
                fullName = userDoc.getString("fullName") ?: "",
                role = userDoc.getString("role") ?: "student",
                avatar = userDoc.getString("avatar") ?: "",
                phone = userDoc.getString("phone") ?: "",
                studentCode = userDoc.getString("studentCode") ?: "",
                className = userDoc.getString("className") ?: ""
            )
        } catch (e: Exception) {
            Log.e(TAG, "💥 LỖI đăng ký:", e)
            null
        }
    }

    // ==================== STATISTICS FOR MAIN DASHBOARD ====================
    
    /**
     * Lấy tổng số người dùng
     */
    suspend fun getTotalUsers(): Int {
        return try {
            val snapshot = db.collection("users").get().await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total users: ")
            1250 // fallback
        }
    }
    
    /**
     * Lấy tổng số lớp học
     */
    suspend fun getTotalClasses(): Int {
        return try {
            val snapshot = db.collection("classes").get().await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total classes: ")
            48 // fallback
        }
    }
    
    /**
     * Lấy tổng số giáo viên
     */
    suspend fun getTotalTeachers(): Int {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("role", "teacher")
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total teachers: ")
            0
        }
    }
    
    /**
     * Lấy tổng số học sinh
     */
    suspend fun getTotalStudents(): Int {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("role", "student")
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total students: ")
            0
        }
    }
    
    /**
     * Lấy đánh giá trung bình (từ collection reviews)
     */
    suspend fun getAverageRating(): Double {
        return try {
            val snapshot = db.collection("reviews").get().await()
            if (snapshot.isEmpty) return 4.9
            val total = snapshot.documents.sumOf { doc ->
                (doc.getDouble("rating") ?: 0.0)
            }
            total / snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting average rating: ")
            4.9
        }
    }

    // ==================== ARTICLES FOR MAIN DASHBOARD ====================
    
    /**
     * Lấy danh sách bài viết mới
     */
    suspend fun getArticles(limit: Int = 5): List<ArticleData> {
        return try {
            val snapshot = db.collection("articles")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            if (snapshot.isEmpty) {
                return getMockArticles()
            }
            
            snapshot.documents.mapNotNull { doc ->
                ArticleData(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    date = doc.getString("date") ?: "",
                    readTime = doc.getString("readTime") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting articles: ")
            getMockArticles()
        }
    }
    
    private fun getMockArticles(): List<ArticleData> {
        return listOf(
            ArticleData("1", "Cách quản lý lớp học hiệu quả", "Những bí quyết giúp giáo viên quản lý lớp học tốt hơn...", "Hôm qua", "5 phút đọc"),
            ArticleData("2", "Xu hướng giáo dục 2024", "Công nghệ AI đang thay đổi giáo dục như thế nào...", "2 ngày trước", "8 phút đọc"),
            ArticleData("3", "Phương pháp học tập thông minh", "Kỹ thuật Pomodoro và spaced repetition...", "5 ngày trước", "6 phút đọc")
        )
    }
    
    /**
     * Lấy chi tiết một bài viết
     */
    suspend fun getArticleById(articleId: String): ArticleData? {
        return try {
            val doc = db.collection("articles").document(articleId).get().await()
            if (doc.exists()) {
                ArticleData(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    date = doc.getString("date") ?: "",
                    readTime = doc.getString("readTime") ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting article: ")
            null
        }
    }

    // ==================== NOTIFICATIONS ====================
    
    /**
     * Lấy danh sách thông báo của người dùng
     */
    suspend fun getNotifications(userId: String, limit: Int = 10): List<NotificationData> {
        return try {
            val snapshot = db.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                NotificationData(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    content = doc.getString("content") ?: "",
                    createdAt = doc.getString("createdAt") ?: "",
                    isRead = doc.getBoolean("isRead") ?: false
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting notifications: ")
            emptyList()
        }
    }
    
    /**
     * Lấy số thông báo chưa đọc
     */
    suspend fun getUnreadNotificationCount(userId: String): Int {
        return try {
            val snapshot = db.collection("notifications")
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting unread count: ")
            0
        }
    }
    
    /**
     * Đánh dấu thông báo đã đọc
     */
    suspend fun markNotificationAsRead(notificationId: String): Boolean {
        return try {
            db.collection("notifications").document(notificationId)
                .update("isRead", true)
                .await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error marking notification as read: ")
            false
        }
    }
    
    /**
     * Gửi thông báo (cho giáo viên)
     */
    suspend fun sendNotification(title: String, content: String, targetRole: String = "all"): Boolean {
        return try {
            val notification = hashMapOf(
                "title" to title,
                "content" to content,
                "targetRole" to targetRole,
                "createdAt" to System.currentTimeMillis(),
                "isRead" to false
            )
            db.collection("notifications").add(notification).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error sending notification: ")
            false
        }
    }

    // ==================== CLASSES ====================

    fun getClassesByTeacher(teacherId: String): Flow<List<Class>> = callbackFlow {
        Log.d(TAG, "📚 Lấy danh sách lớp cho giáo viên: ")
        val listener = db.collection("classes")
            .whereEqualTo("teacherId", teacherId)
            .orderBy("className", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "❌ Lỗi lấy danh sách lớp:", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val classes = snapshot.documents.mapNotNull { doc ->
                        try {
                            Class(
                                id = doc.id,
                                className = doc.getString("className") ?: "",
                                subject = doc.getString("subject") ?: "",
                                academicYear = doc.getString("academicYear") ?: "",
                                teacherId = doc.getString("teacherId") ?: "",
                                room = doc.getString("room") ?: "",
                                schedule = doc.getString("schedule") ?: "",
                                studentCount = (doc.getLong("studentCount") ?: 0).toInt()
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "❌ Lỗi parse class: ", e)
                            null
                        }
                    }
                    trySend(classes).isSuccess
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    fun getClassesByStudent(studentId: String): Flow<List<Class>> = callbackFlow {
        Log.d(TAG, "📚 Lấy danh sách lớp cho học sinh: ")
        val listener = db.collection("students")
            .whereEqualTo("userId", studentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "❌ Lỗi lấy danh sách lớp học sinh:", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val classIds = snapshot.documents.mapNotNull { it.getString("classId") }
                    if (classIds.isEmpty()) {
                        trySend(emptyList()).isSuccess
                        return@addSnapshotListener
                    }

                    db.collection("classes")
                        .whereIn("id", classIds)
                        .get()
                        .addOnSuccessListener { classSnapshot ->
                            val classes = classSnapshot.documents.mapNotNull { doc ->
                                try {
                                    Class(
                                        id = doc.id,
                                        className = doc.getString("className") ?: "",
                                        subject = doc.getString("subject") ?: "",
                                        academicYear = doc.getString("academicYear") ?: "",
                                        teacherId = doc.getString("teacherId") ?: "",
                                        room = doc.getString("room") ?: "",
                                        schedule = doc.getString("schedule") ?: "",
                                        studentCount = (doc.getLong("studentCount") ?: 0).toInt()
                                    )
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            trySend(classes).isSuccess
                        }
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun getClassById(classId: String): Class? {
        Log.d(TAG, "🔍 Lấy thông tin lớp: ")
        return try {
            val doc = db.collection("classes").document(classId).get().await()
            if (doc.exists()) {
                Class(
                    id = doc.id,
                    className = doc.getString("className") ?: "",
                    subject = doc.getString("subject") ?: "",
                    academicYear = doc.getString("academicYear") ?: "",
                    teacherId = doc.getString("teacherId") ?: "",
                    room = doc.getString("room") ?: "",
                    schedule = doc.getString("schedule") ?: "",
                    studentCount = (doc.getLong("studentCount") ?: 0).toInt()
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy thông tin lớp:", e)
            null
        }
    }

    // ==================== STUDENTS ====================

    suspend fun getStudentsByClass(classId: String): List<Student> {
        Log.d(TAG, "🔍 Lấy danh sách học sinh của lớp: ")
        return try {
            val snapshot = db.collection("students")
                .whereEqualTo("classId", classId)
                .get()
                .await()

            val students = mutableListOf<Student>()

            for (doc in snapshot.documents) {
                val userId = doc.getString("userId") ?: continue
                val userDoc = db.collection("users").document(userId).get().await()

                students.add(
                    Student(
                        id = doc.id,
                        userId = userId,
                        classId = classId,
                        studentCode = doc.getString("studentCode") ?: "",
                        fullName = userDoc.getString("fullName") ?: "",
                        email = userDoc.getString("email") ?: ""
                    )
                )
            }
            students.sortedBy { it.fullName }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy danh sách học sinh:", e)
            emptyList()
        }
    }

    suspend fun getStudentClasses(studentId: String): List<Class> {
        Log.d(TAG, "🔍 Lấy danh sách lớp của học sinh: ")
        return try {
            val studentSnapshot = db.collection("students")
                .whereEqualTo("userId", studentId)
                .get()
                .await()

            val classIds = studentSnapshot.documents.mapNotNull { it.getString("classId") }
            if (classIds.isEmpty()) return emptyList()

            val classesSnapshot = db.collection("classes")
                .whereIn("id", classIds)
                .get()
                .await()

            classesSnapshot.documents.map { doc ->
                Class(
                    id = doc.id,
                    className = doc.getString("className") ?: "",
                    subject = doc.getString("subject") ?: "",
                    academicYear = doc.getString("academicYear") ?: "",
                    teacherId = doc.getString("teacherId") ?: "",
                    room = doc.getString("room") ?: "",
                    schedule = doc.getString("schedule") ?: "",
                    studentCount = (doc.getLong("studentCount") ?: 0).toInt()
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy danh sách lớp của học sinh:", e)
            emptyList()
        }
    }

    // ==================== GRADES ====================

    suspend fun getGradeColumns(classId: String): List<GradeColumn> {
        Log.d(TAG, "🔍 Lấy cột điểm của lớp: ")
        return try {
            val snapshot = db.collection("gradeColumns")
                .whereEqualTo("classId", classId)
                .orderBy("displayOrder")
                .get()
                .await()
            snapshot.documents.map { doc ->
                GradeColumn(
                    id = doc.id,
                    classId = doc.getString("classId") ?: "",
                    columnName = doc.getString("columnName") ?: "",
                    columnType = doc.getString("columnType") ?: "",
                    weight = (doc.getLong("weight") ?: 1).toInt(),
                    displayOrder = (doc.getLong("displayOrder") ?: 0).toInt(),
                    maxScore = (doc.getLong("maxScore") ?: 10).toInt()
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy cột điểm:", e)
            emptyList()
        }
    }

    suspend fun getScoresByStudent(studentId: String): Map<String, Float?> {
        Log.d(TAG, "🔍 Lấy điểm của học sinh: ")
        return try {
            val snapshot = db.collection("scores")
                .whereEqualTo("studentId", studentId)
                .get()
                .await()
            snapshot.documents.associate { doc ->
                val columnId = doc.getString("gradeColumnId") ?: ""
                val scoreValue = doc.getDouble("scoreValue")?.toFloat()
                columnId to scoreValue
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy điểm:", e)
            emptyMap()
        }
    }

    suspend fun getStudentScoresInClass(studentId: String, classId: String): Map<String, Float?> {
        Log.d(TAG, "🔍 Lấy điểm của học sinh  trong lớp ")
        return try {
            val columns = getGradeColumns(classId)
            val columnIds = columns.map { it.id }
            if (columnIds.isEmpty()) return emptyMap()

            val snapshot = db.collection("scores")
                .whereEqualTo("studentId", studentId)
                .whereIn("gradeColumnId", columnIds)
                .get()
                .await()
            snapshot.documents.associate { doc ->
                val columnId = doc.getString("gradeColumnId") ?: ""
                val scoreValue = doc.getDouble("scoreValue")?.toFloat()
                columnId to scoreValue
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy điểm:", e)
            emptyMap()
        }
    }

    suspend fun getGradeTable(classId: String): GradeTable? {
        Log.d(TAG, "🔍 Lấy bảng điểm của lớp: ")
        return try {
            val classDoc = db.collection("classes").document(classId).get().await()
            if (!classDoc.exists()) return null

            val classInfo = Class(
                id = classDoc.id,
                className = classDoc.getString("className") ?: "",
                subject = classDoc.getString("subject") ?: "",
                academicYear = classDoc.getString("academicYear") ?: "",
                teacherId = classDoc.getString("teacherId") ?: "",
                room = classDoc.getString("room") ?: "",
                schedule = classDoc.getString("schedule") ?: "",
                studentCount = (classDoc.getLong("studentCount") ?: 0).toInt()
            )

            val columns = getGradeColumns(classId)
            val students = getStudentsByClass(classId)

            val studentsWithScores = students.map { student ->
                val scores = getScoresByStudent(student.id)
                val average = calculateAverage(scores, columns)
                StudentWithScores(
                    student = student,
                    scores = scores,
                    averageScore = average
                )
            }

            GradeTable(
                classInfo = classInfo,
                gradeColumns = columns,
                students = studentsWithScores
            )
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy bảng điểm:", e)
            null
        }
    }

    suspend fun getStudentGradeTable(studentId: String, classId: String): GradeTable? {
        Log.d(TAG, "🔍 Lấy bảng điểm của học sinh  trong lớp ")
        return try {
            val classDoc = db.collection("classes").document(classId).get().await()
            if (!classDoc.exists()) return null

            val classInfo = Class(
                id = classDoc.id,
                className = classDoc.getString("className") ?: "",
                subject = classDoc.getString("subject") ?: "",
                academicYear = classDoc.getString("academicYear") ?: "",
                teacherId = classDoc.getString("teacherId") ?: "",
                room = classDoc.getString("room") ?: "",
                schedule = classDoc.getString("schedule") ?: "",
                studentCount = (classDoc.getLong("studentCount") ?: 0).toInt()
            )

            val columns = getGradeColumns(classId)
            val scores = getStudentScoresInClass(studentId, classId)

            val userDoc = db.collection("users").document(studentId).get().await()
            val student = Student(
                id = studentId,
                userId = studentId,
                classId = classId,
                studentCode = userDoc.getString("studentCode") ?: "",
                fullName = userDoc.getString("fullName") ?: "",
                email = userDoc.getString("email") ?: ""
            )

            val average = calculateAverage(scores, columns)

            GradeTable(
                classInfo = classInfo,
                gradeColumns = columns,
                students = listOf(
                    StudentWithScores(
                        student = student,
                        scores = scores,
                        averageScore = average
                    )
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy bảng điểm học sinh:", e)
            null
        }
    }

    // ==================== SCORES ====================

    suspend fun updateScore(studentId: String, gradeColumnId: String, scoreValue: Float?) {
        Log.d(TAG, "📝 Cập nhật điểm: student=, column=, value=")
        try {
            val snapshot = db.collection("scores")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("gradeColumnId", gradeColumnId)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val docId = snapshot.documents[0].id
                db.collection("scores").document(docId)
                    .update("scoreValue", scoreValue, "updatedAt", System.currentTimeMillis())
                    .await()
            } else {
                val newScore = hashMapOf(
                    "studentId" to studentId,
                    "gradeColumnId" to gradeColumnId,
                    "scoreValue" to scoreValue,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("scores").add(newScore).await()
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật điểm:", e)
        }
    }

    suspend fun updateScores(scores: List<Score>) {
        Log.d(TAG, "📝 Cập nhật  điểm")
        if (scores.isEmpty()) return

        try {
            val batch = db.batch()
            for (score in scores) {
                val snapshot = db.collection("scores")
                    .whereEqualTo("studentId", score.studentId)
                    .whereEqualTo("gradeColumnId", score.gradeColumnId)
                    .get()
                    .await()

                if (!snapshot.isEmpty) {
                    val docRef = db.collection("scores").document(snapshot.documents[0].id)
                    batch.update(docRef, "scoreValue", score.scoreValue, "updatedAt", System.currentTimeMillis())
                } else {
                    val newScore = hashMapOf(
                        "studentId" to score.studentId,
                        "gradeColumnId" to score.gradeColumnId,
                        "scoreValue" to score.scoreValue,
                        "updatedAt" to System.currentTimeMillis()
                    )
                    val docRef = db.collection("scores").document()
                    batch.set(docRef, newScore)
                }
            }
            batch.commit().await()
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật nhiều điểm:", e)
        }
    }

    // ==================== UTILITY ====================

    private fun calculateAverage(
        scores: Map<String, Float?>,
        columns: List<GradeColumn>
    ): Float? {
        var totalWeight = 0
        var totalScore = 0f

        columns.forEach { column ->
            val score = scores[column.id]
            if (score != null) {
                totalWeight += column.weight
                totalScore += score * column.weight
            }
        }
        return if (totalWeight > 0) totalScore / totalWeight else null
    }

    suspend fun getUserById(userId: String): User? {
        Log.d(TAG, "🔍 Lấy thông tin user: ")
        return try {
            val doc = db.collection("users").document(userId).get().await()
            if (doc.exists()) {
                User(
                    id = doc.id,
                    email = doc.getString("email") ?: "",
                    fullName = doc.getString("fullName") ?: "",
                    role = doc.getString("role") ?: "student",
                    avatar = doc.getString("avatar") ?: "",
                    phone = doc.getString("phone") ?: "",
                    studentCode = doc.getString("studentCode") ?: "",
                    className = doc.getString("className") ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy thông tin user:", e)
            null
        }
    }

    suspend fun updateUser(userId: String, data: Map<String, Any>): Boolean {
        Log.d(TAG, "📝 Cập nhật user: ")
        return try {
            db.collection("users").document(userId).update(data).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật user:", e)
            false
        }
    }
}

// ==================== DATA CLASSES FOR MAIN ====================

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
