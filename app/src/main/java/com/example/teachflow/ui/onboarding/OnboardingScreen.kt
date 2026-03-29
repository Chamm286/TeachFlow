package com.example.teachflow.ui.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val onboardingItems = listOf(
        OnboardingItem(
            title = "TeachFlow",
            description = "Trợ lý giáo viên thông minh\nGiải pháp quản lý lớp học hiện đại",
            imageRes = R.drawable.onboarding1
        ),
        OnboardingItem(
            title = "Quản lý lớp học",
            description = "Dễ dàng quản lý danh sách lớp, điểm số và bài tập\nMọi thông tin được đồng bộ hóa",
            imageRes = R.drawable.onboarding2
        ),
        OnboardingItem(
            title = "Theo dõi tiến độ",
            description = "Báo cáo chi tiết về tiến độ học tập của từng học sinh\nĐánh giá và phân tích thông minh",
            imageRes = R.drawable.onboarding3
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingItems.size })
    val coroutineScope = rememberCoroutineScope()

    // Auto slide đúng cách
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            // Chỉ auto slide nếu không phải trang cuối
            if (pagerState.currentPage < onboardingItems.size - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    // Animation nền động
    val infiniteTransition = rememberInfiniteTransition(label = "bg_animation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    val x1 = (cos(Math.toRadians(angle.toDouble())) * 400f).toFloat()
    val y1 = (sin(Math.toRadians(angle.toDouble())) * 300f).toFloat()
    val x2 = (cos(Math.toRadians((angle + 120).toDouble())) * 500f).toFloat()
    val y2 = (sin(Math.toRadians((angle + 120).toDouble())) * 400f).toFloat()

    // Màu sắc chủ đạo
    val accentColor = Color(0xFF6366F1)
    val bgStart = Color(0xFFF5F3FF)
    val bgEnd = Color(0xFFEEF2FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(bgStart, bgEnd)
                )
            )
    ) {
        // Hiệu ứng gradient động
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            Color.Transparent,
                            Color.Transparent
                        ),
                        radius = 800f,
                        center = Offset(300f + x1, 400f + y1)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFC4B5FD).copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        radius = 900f,
                        center = Offset(500f + x2, 600f + y2)
                    )
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val imageScale = 1f - pageOffset.absoluteValue * 0.1f
            val imageAlpha = 1f - pageOffset.absoluteValue * 0.15f
            val textOffsetY = pageOffset * 30f
            val textAlpha = 1f - pageOffset.absoluteValue.coerceIn(0f, 0.3f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Ảnh minh họa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .scale(imageScale.coerceIn(0.9f, 1f))
                        .alpha(imageAlpha.coerceIn(0.85f, 1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(32.dp),
                                clip = false,
                                ambientColor = accentColor.copy(alpha = 0.2f),
                                spotColor = accentColor.copy(alpha = 0.15f)
                            ),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(id = onboardingItems[page].imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nội dung text
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                        .offset(y = textOffsetY.dp)
                        .alpha(textAlpha),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = onboardingItems[page].title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = onboardingItems[page].description,
                        fontSize = 15.sp,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dấu chấm chỉ mục
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingItems.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(width = if (isSelected) 28.dp else 8.dp, height = 8.dp)
                                .padding(horizontal = 3.dp)
                                .background(
                                    if (isSelected) accentColor else Color(0xFFCBD5E1),
                                    RoundedCornerShape(4.dp)
                                )
                                .animateContentSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nút bấm
                if (pagerState.currentPage == onboardingItems.size - 1) {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "BẮT ĐẦU",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = accentColor
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.5.dp
                        )
                    ) {
                        Text(
                            text = "TIẾP THEO",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // Nút Skip
        Text(
            text = "Bỏ qua",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF94A3B8),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .clickable { onComplete() }
        )
    }
}