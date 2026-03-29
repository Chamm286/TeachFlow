package com.example.teachflow.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

// Màu sắc hiện đại - Xanh dương chủ đạo
val SplashPrimary = Color(0xFF2196F3)
val SplashPrimaryDark = Color(0xFF1976D2)
val SplashPrimaryLight = Color(0xFF64B5F6)
val SplashAccent = Color(0xFF00BCD4)
val SplashGradientStart = Color(0xFF1E88E5)
val SplashGradientEnd = Color(0xFF0D47A1)

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    // Animation cho logo
    val infiniteTransition = rememberInfiniteTransition(label = "logo")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alphaLogo by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaLogo"
    )
    
    // Animation cho background gradient động
    val bgTransition = rememberInfiniteTransition(label = "bg")
    val angle by bgTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )
    
    val offsetX = (cos(Math.toRadians(angle.toDouble())) * 150f).toFloat()
    val offsetY = (sin(Math.toRadians(angle.toDouble())) * 150f).toFloat()
    
    // Animation cho text
    var startAnimation by remember { mutableStateOf(false) }
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "textAlpha"
    )
    
    val textOffset by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 40.dp,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "textOffset"
    )
    
    // Dot loading animation
    val dot1 by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    
    val dot2 by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, delayMillis = 150),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    
    val dot3 by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, delayMillis = 300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2800)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SplashGradientStart,
                        SplashPrimary,
                        SplashPrimaryDark
                    )
                )
            )
    ) {
        // Hiệu ứng ánh sáng động
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SplashPrimaryLight.copy(alpha = 0.15f),
                            Color.Transparent,
                            Color.Transparent
                        ),
                        radius = 600f,
                        center = Offset(300f + offsetX, 400f + offsetY)
                    )
                )
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SplashAccent.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        radius = 500f,
                        center = Offset(200f - offsetX * 0.5f, 500f - offsetY * 0.5f)
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Card với hiệu ứng
            Card(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale)
                    .alpha(alphaLogo),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📚",
                        fontSize = 80.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Text với animation
            Column(
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TeachFlow",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Trợ lý giáo viên thông minh",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Loading dots
                Row(
                    modifier = Modifier.alpha(textAlpha),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dot1)
                            .background(
                                Color.White,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dot2)
                            .background(
                                Color.White,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dot3)
                            .background(
                                Color.White,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
        }
        
        // Copyright
        Text(
            text = "© 2026 TeachFlow Team",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
