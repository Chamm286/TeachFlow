package com.example.teachflow.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.ui.theme.Primary
import com.example.teachflow.ui.theme.Secondary
import com.example.teachflow.ui.theme.StudentColor
import com.example.teachflow.ui.theme.TeacherColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onComplete) {
                Text("Bỏ qua", color = Primary)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.size(180.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when(page) {
                            0 -> Primary.copy(alpha = 0.1f)
                            1 -> Secondary.copy(alpha = 0.1f)
                            2 -> StudentColor.copy(alpha = 0.1f)
                            else -> TeacherColor.copy(alpha = 0.1f)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when(page) {
                                0 -> "📊"
                                1 -> "👥"
                                2 -> "📈"
                                else -> "🔐"
                            },
                            fontSize = 80.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = when(page) {
                        0 -> "Quản lý điểm số"
                        1 -> "Quản lý lớp học"
                        2 -> "Thống kê chi tiết"
                        else -> "An toàn bảo mật"
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = when(page) {
                        0 -> Primary
                        1 -> Secondary
                        2 -> StudentColor
                        else -> TeacherColor
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when(page) {
                        0 -> "Nhập điểm nhanh chóng, tự động tính điểm trung bình theo hệ số"
                        1 -> "Dễ dàng tạo và quản lý danh sách lớp, học sinh"
                        2 -> "Xem báo cáo, biểu đồ phân tích kết quả học tập"
                        else -> "Đăng nhập an toàn, phân quyền rõ ràng"
                    },
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    when(iteration) {
                        0 -> Primary
                        1 -> Secondary
                        2 -> StudentColor
                        else -> TeacherColor
                    }
                else Color.LightGray

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (pagerState.currentPage == iteration) 24.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = when(pagerState.currentPage) {
                    0 -> Primary
                    1 -> Secondary
                    2 -> StudentColor
                    else -> TeacherColor
                }
            )
        ) {
            Text(
                text = "Bắt đầu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}