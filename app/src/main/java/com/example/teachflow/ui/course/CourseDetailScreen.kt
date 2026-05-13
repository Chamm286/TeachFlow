package com.example.teachflow.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.ui.main.PremiumIconBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseTitle: String
) {
    // Màu sắc giả định (có thể lấy từ theme sau)
    val primaryColor = Color(0xFF6366F1)
    val accentColor = Color(0xFFEC4899)
    val surfaceColor = Color.White
    val textPrimaryColor = Color(0xFF1A1A2E)
    val textSecondaryColor = Color(0xFF666666)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết lớp học", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Rounded.Share, contentDescription = "Chia sẻ")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Học phí", fontSize = 12.sp, color = textSecondaryColor)
                        Text("Miễn phí", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = primaryColor)
                    }
                    Button(
                        onClick = { /* Đăng ký */ },
                        modifier = Modifier
                            .height(56.dp)
                            .weight(1.5f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text("Tham gia ngay", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Image / Gradient
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(primaryColor, accentColor)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        PremiumIconBox(
                            icon = Icons.Rounded.School,
                            color = Color.White.copy(alpha = 0.2f),
                            size = 80.dp,
                            iconSize = 40.dp,
                            shape = CircleShape
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = courseTitle,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBadge(icon = Icons.Rounded.People, text = "1.2K Học viên")
                    InfoBadge(icon = Icons.Rounded.Star, text = "4.9 (240)")
                    InfoBadge(icon = Icons.Rounded.Timer, text = "12 Buổi")
                }
            }

            // Description
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Giới thiệu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Khóa học này cung cấp kiến thức nền tảng và nâng cao về $courseTitle. Học viên sẽ được thực hành trực tiếp với các dự án thực tế, dưới sự hướng dẫn của đội ngũ giáo viên giàu kinh nghiệm.",
                        fontSize = 14.sp,
                        color = textSecondaryColor,
                        lineHeight = 22.sp
                    )
                }
            }

            // Benefits
            item {
                Text(
                    "Bạn sẽ học được gì?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                BenefitItem(text = "Kiến thức thực tế, áp dụng được ngay")
                BenefitItem(text = "Tương tác trực tiếp với giáo viên qua AI")
                BenefitItem(text = "Cấp chứng chỉ hoàn thành sau khóa học")
                BenefitItem(text = "Tham gia cộng đồng học viên lớn mạnh")
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun InfoBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, contentDescription = null, size = 16.dp, tint = Color(0xFF6366F1))
        Text(text, fontSize = 12.sp, color = Color(0xFF666666))
    }
}

@Composable
fun BenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE0E7FF),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF6366F1))
            }
        }
        Text(text, fontSize = 14.sp, color = Color(0xFF4B5563))
    }
}

@Composable
private fun Icon(icon: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(size), tint = tint)
}
