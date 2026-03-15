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

    /**
     * Đăng nhập người dùng bằng email
     * @param email Email người dùng
     * @param password Mật khẩu (không dùng trong demo)
     * @return User object nếu tìm thấy, null nếu không
     */
    suspend fun login(email: String, password: String): User? {
        Log.d(TAG, "🔍 Đang tìm email: $email")
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            Log.d(TAG, "📊 Số lượng documents tìm thấy: ${snapshot.size()}")

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                Log.d(TAG, "✅ Tìm thấy user: ${doc.id}")
                Log.d(TAG, "   Email: ${doc.getString("email")}")
                Log.d(TAG, "   FullName: ${doc.getString("fullName")}")
                Log.d(TAG, "   Role: ${doc.getString("role")}")

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
                Log.e(TAG, "❌ KHÔNG tìm thấy email: $email")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 LỖI Firebase:", e)
            null
        }
    }

    /**
     * Đăng ký tài khoản mới
     * @param email Email
     * @param password Mật khẩu (không lưu)
     * @param fullName Họ tên
     * @param role Vai trò (teacher/student)
     * @param studentCode Mã HS (nếu là student)
     * @param className Lớp (nếu là student)
     * @return User object nếu thành công, null nếu thất bại
     */
    suspend fun register(
        email: String,
        password: String, // Không lưu password trong demo
        fullName: String,
        role: String,
        studentCode: String = "",
        className: String = ""
    ): User? {
        Log.d(TAG, "📝 Đăng ký tài khoản mới: $email")
        return try {
            // Kiểm tra email đã tồn tại chưa
            val existingUser = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!existingUser.isEmpty) {
                Log.e(TAG, "❌ Email đã tồn tại: $email")
                return null
            }

            // Tạo avatar tự động từ tên
            val avatarUrl = "https://ui-avatars.com/api/?name=${fullName.replace(" ", "+")}&size=128&background=0D8F81&color=fff"

            // Tạo user mới
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
            Log.d(TAG, "✅ Đã tạo user với ID: ${docRef.id}")

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

    // ==================== CLASSES ====================

    /**
     * Lấy danh sách lớp theo giáo viên (Realtime)
     * @param teacherId ID của giáo viên
     * @return Flow danh sách lớp
     */
    fun getClassesByTeacher(teacherId: String): Flow<List<Class>> = callbackFlow {
        Log.d(TAG, "📚 Lấy danh sách lớp cho giáo viên: $teacherId")
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
                    Log.d(TAG, "📊 Tìm thấy ${snapshot.size()} lớp")
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
                            Log.e(TAG, "❌ Lỗi parse class: ${doc.id}", e)
                            null
                        }
                    }
                    trySend(classes).isSuccess
                }
            }

        awaitClose {
            Log.d(TAG, "🛑 Đóng listener classes")
            listener.remove()
        }
    }

    /**
     * Lấy danh sách lớp của học sinh (Realtime)
     * @param studentId ID của học sinh
     * @return Flow danh sách lớp
     */
    fun getClassesByStudent(studentId: String): Flow<List<Class>> = callbackFlow {
        Log.d(TAG, "📚 Lấy danh sách lớp cho học sinh: $studentId")
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
                    Log.d(TAG, "📊 Tìm thấy ${classIds.size} classIds")

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
                                    Log.e(TAG, "❌ Lỗi parse class: ${doc.id}", e)
                                    null
                                }
                            }
                            Log.d(TAG, "✅ Lấy được ${classes.size} lớp")
                            trySend(classes).isSuccess
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "❌ Lỗi lấy chi tiết lớp:", e)
                        }
                }
            }

        awaitClose {
            Log.d(TAG, "🛑 Đóng listener student classes")
            listener.remove()
        }
    }

    /**
     * Lấy thông tin chi tiết một lớp
     * @param classId ID của lớp
     * @return Class object hoặc null
     */
    suspend fun getClassById(classId: String): Class? {
        Log.d(TAG, "🔍 Lấy thông tin lớp: $classId")
        return try {
            val doc = db.collection("classes").document(classId).get().await()
            if (doc.exists()) {
                Log.d(TAG, "✅ Tìm thấy lớp: ${doc.getString("className")}")
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
                Log.e(TAG, "❌ Không tìm thấy lớp: $classId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy thông tin lớp:", e)
            null
        }
    }

    // ==================== STUDENTS ====================

    /**
     * Lấy danh sách học sinh của một lớp
     * @param classId ID của lớp
     * @return List<Student>
     */
    suspend fun getStudentsByClass(classId: String): List<Student> {
        Log.d(TAG, "🔍 Lấy danh sách học sinh của lớp: $classId")
        return try {
            val snapshot = db.collection("students")
                .whereEqualTo("classId", classId)
                .get()
                .await()

            Log.d(TAG, "📊 Tìm thấy ${snapshot.size()} học sinh trong bảng students")

            val students = mutableListOf<Student>()

            for (doc in snapshot.documents) {
                val userId = doc.getString("userId") ?: continue
                Log.d(TAG, "   - Lấy thông tin user: $userId")

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

            Log.d(TAG, "✅ Lấy được ${students.size} học sinh")
            students.sortedBy { it.fullName }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy danh sách học sinh:", e)
            emptyList()
        }
    }

    /**
     * Lấy danh sách lớp của học sinh
     * @param studentId ID của học sinh
     * @return List<Class>
     */
    suspend fun getStudentClasses(studentId: String): List<Class> {
        Log.d(TAG, "🔍 Lấy danh sách lớp của học sinh: $studentId")
        return try {
            val studentSnapshot = db.collection("students")
                .whereEqualTo("userId", studentId)
                .get()
                .await()

            val classIds = studentSnapshot.documents.mapNotNull { it.getString("classId") }
            Log.d(TAG, "📊 Tìm thấy ${classIds.size} classIds")

            if (classIds.isEmpty()) return emptyList()

            val classesSnapshot = db.collection("classes")
                .whereIn("id", classIds)
                .get()
                .await()

            val classes = classesSnapshot.documents.map { doc ->
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
            Log.d(TAG, "✅ Lấy được ${classes.size} lớp")
            classes
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy danh sách lớp của học sinh:", e)
            emptyList()
        }
    }

    // ==================== GRADES ====================

    /**
     * Lấy cột điểm của một lớp
     * @param classId ID của lớp
     * @return List<GradeColumn>
     */
    suspend fun getGradeColumns(classId: String): List<GradeColumn> {
        Log.d(TAG, "🔍 Lấy cột điểm của lớp: $classId")
        return try {
            val snapshot = db.collection("gradeColumns")
                .whereEqualTo("classId", classId)
                .orderBy("displayOrder")
                .get()
                .await()

            Log.d(TAG, "📊 Tìm thấy ${snapshot.size()} cột điểm")

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

    /**
     * Lấy điểm của một học sinh
     * @param studentId ID của học sinh
     * @return Map<gradeColumnId, scoreValue>
     */
    suspend fun getScoresByStudent(studentId: String): Map<String, Float?> {
        Log.d(TAG, "🔍 Lấy điểm của học sinh: $studentId")
        return try {
            val snapshot = db.collection("scores")
                .whereEqualTo("studentId", studentId)
                .get()
                .await()

            Log.d(TAG, "📊 Tìm thấy ${snapshot.size()} điểm")

            snapshot.documents.associate { doc ->
                val columnId = doc.getString("gradeColumnId") ?: ""
                val scoreValue = if (doc.contains("scoreValue")) {
                    (doc.getDouble("scoreValue"))?.toFloat()
                } else null
                columnId to scoreValue
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy điểm:", e)
            emptyMap()
        }
    }

    /**
     * Lấy điểm của một học sinh trong một lớp
     * @param studentId ID học sinh
     * @param classId ID lớp
     * @return Map<gradeColumnId, scoreValue>
     */
    suspend fun getStudentScoresInClass(studentId: String, classId: String): Map<String, Float?> {
        Log.d(TAG, "🔍 Lấy điểm của học sinh $studentId trong lớp $classId")
        return try {
            val columns = getGradeColumns(classId)
            val columnIds = columns.map { it.id }

            if (columnIds.isEmpty()) {
                Log.d(TAG, "📊 Lớp không có cột điểm nào")
                return emptyMap()
            }

            val snapshot = db.collection("scores")
                .whereEqualTo("studentId", studentId)
                .whereIn("gradeColumnId", columnIds)
                .get()
                .await()

            Log.d(TAG, "📊 Tìm thấy ${snapshot.size()} điểm")

            snapshot.documents.associate { doc ->
                val columnId = doc.getString("gradeColumnId") ?: ""
                val scoreValue = if (doc.contains("scoreValue")) {
                    (doc.getDouble("scoreValue"))?.toFloat()
                } else null
                columnId to scoreValue
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy điểm:", e)
            emptyMap()
        }
    }

    /**
     * Lấy bảng điểm hoàn chỉnh của một lớp
     * @param classId ID lớp
     * @return GradeTable hoặc null
     */
    suspend fun getGradeTable(classId: String): GradeTable? {
        Log.d(TAG, "🔍 Lấy bảng điểm của lớp: $classId")
        return try {
            val classDoc = db.collection("classes").document(classId).get().await()
            if (!classDoc.exists()) {
                Log.e(TAG, "❌ Không tìm thấy lớp: $classId")
                return null
            }

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

            Log.d(TAG, "✅ Lấy bảng điểm thành công: ${students.size} học sinh, ${columns.size} cột điểm")

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

    /**
     * Lấy bảng điểm của một học sinh
     * @param studentId ID học sinh
     * @param classId ID lớp
     * @return GradeTable chỉ có 1 học sinh
     */
    suspend fun getStudentGradeTable(studentId: String, classId: String): GradeTable? {
        Log.d(TAG, "🔍 Lấy bảng điểm của học sinh $studentId trong lớp $classId")
        return try {
            val classDoc = db.collection("classes").document(classId).get().await()
            if (!classDoc.exists()) {
                Log.e(TAG, "❌ Không tìm thấy lớp: $classId")
                return null
            }

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

            Log.d(TAG, "✅ Lấy bảng điểm học sinh thành công")

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

    /**
     * Cập nhật điểm cho một học sinh
     * @param studentId ID học sinh
     * @param gradeColumnId ID cột điểm
     * @param scoreValue Giá trị điểm
     */
    suspend fun updateScore(studentId: String, gradeColumnId: String, scoreValue: Float?) {
        Log.d(TAG, "📝 Cập nhật điểm: student=$studentId, column=$gradeColumnId, value=$scoreValue")
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
                Log.d(TAG, "✅ Đã cập nhật điểm")
            } else {
                val newScore = hashMapOf(
                    "studentId" to studentId,
                    "gradeColumnId" to gradeColumnId,
                    "scoreValue" to scoreValue,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("scores").add(newScore).await()
                Log.d(TAG, "✅ Đã thêm điểm mới")
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật điểm:", e)
        }
    }

    /**
     * Cập nhật nhiều điểm cùng lúc (Batch)
     * @param scores List<Score>
     */
    suspend fun updateScores(scores: List<Score>) {
        Log.d(TAG, "📝 Cập nhật ${scores.size} điểm")
        if (scores.isEmpty()) {
            Log.d(TAG, "📝 Không có điểm để cập nhật")
            return
        }

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
            Log.d(TAG, "✅ Cập nhật thành công ${scores.size} điểm")
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật nhiều điểm:", e)
        }
    }

    // ==================== UTILITY ====================

    /**
     * Tính điểm trung bình
     * @param scores Map điểm
     * @param columns Danh sách cột điểm
     * @return Điểm trung bình
     */
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

        val average = if (totalWeight > 0) totalScore / totalWeight else null
        Log.d(TAG, "📊 Điểm trung bình: $average (tổng hệ số: $totalWeight)")
        return average
    }

    /**
     * Lấy thông tin người dùng theo ID
     * @param userId ID người dùng
     * @return User object hoặc null
     */
    suspend fun getUserById(userId: String): User? {
        Log.d(TAG, "🔍 Lấy thông tin user: $userId")
        return try {
            val doc = db.collection("users").document(userId).get().await()
            if (doc.exists()) {
                Log.d(TAG, "✅ Tìm thấy user: ${doc.getString("email")}")
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
                Log.e(TAG, "❌ Không tìm thấy user: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi lấy thông tin user:", e)
            null
        }
    }

    /**
     * Cập nhật thông tin người dùng
     * @param userId ID người dùng
     * @param data Map dữ liệu cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    suspend fun updateUser(userId: String, data: Map<String, Any>): Boolean {
        Log.d(TAG, "📝 Cập nhật user: $userId")
        return try {
            db.collection("users").document(userId).update(data).await()
            Log.d(TAG, "✅ Cập nhật user thành công")
            true
        } catch (e: Exception) {
            Log.e(TAG, "💥 Lỗi cập nhật user:", e)
            false
        }
    }
}