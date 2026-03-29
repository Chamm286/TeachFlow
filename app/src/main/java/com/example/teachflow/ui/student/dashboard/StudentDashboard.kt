package com.example.teachflow.ui.student.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(
    navController: NavController
) {
    val greeting = remember {
        when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Chào buổi sáng"
            in 12..13 -> "Chào buổi trưa"
            in 14..17 -> "Chào buổi chiều"
            else -> "Chào buổi tối"
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = ", Hoàn!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Học sinh lớp 12A1",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Thông báo */ }) {
                        Badge(
                            containerColor = Error,
                            modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                        ) {
                            Text("3")
                        }
                        Icon(Icons.Default.Notifications, contentDescription = "Thông báo", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(PrimaryGradient)
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "📊 Tổng quan học tập",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Điểm trung bình học kỳ I",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "8.5",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Đã học",
                        value = "45",
                        unit = "buổi",
                        icon = Icons.Default.School,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Điểm TB",
                        value = "8.5",
                        unit = "/10",
                        icon = Icons.Default.Grade,
                        color = Success,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Xếp hạng",
                        value = "12",
                        unit = "/45",
                        icon = Icons.Default.EmojiEvents,
                        color = Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Recent Grades
            item {
                Text(
                    text = "Điểm gần đây",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(listOf(
                GradeItem("Toán", 9.0, "Giỏi"),
                GradeItem("Văn", 8.0, "Khá"),
                GradeItem("Anh", 8.5, "Giỏi"),
                GradeItem("Lý", 7.5, "Khá")
            )) { grade ->
                GradeCard(grade)
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = " ",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

data class GradeItem(
    val subject: String,
    val score: Double,
    val rank: String
)

@Composable
fun GradeCard(grade: GradeItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = grade.subject,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "Điểm: ",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = when(grade.rank) {
                    "Giỏi" -> Success.copy(alpha = 0.1f)
                    "Khá" -> Warning.copy(alpha = 0.1f)
                    else -> Error.copy(alpha = 0.1f)
                }
            ) {
                Text(
                    text = grade.rank,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = when(grade.rank) {
                        "Giỏi" -> Success
                        "Khá" -> Warning
                        else -> Error
                    }
                )
            }
        }
    }
}
