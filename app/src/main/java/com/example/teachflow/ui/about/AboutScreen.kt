package com.example.teachflow.ui.about

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val primaryColor = Color(0xFF2196F3)
    val accentColor = Color(0xFF00BCD4)
    val surfaceColor = Color.White
    val backgroundColor = Color(0xFFF5F7FA)
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Về chúng tôi", 
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        letterSpacing = (-0.5).sp
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White, CircleShape)
                            .shadow(2.dp, CircleShape)
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Quay lại", tint = primaryColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Section: Logo & Name
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(15.dp, RoundedCornerShape(32.dp), spotColor = primaryColor.copy(alpha = 0.5f))
                    .background(
                        brush = Brush.linearGradient(listOf(primaryColor, accentColor)),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "TeachFlow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1A1A2E),
                letterSpacing = (-1).sp
            )
            
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = primaryColor.copy(alpha = 0.1f),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Version 2026.1.0",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Description Card
            AboutInfoCard(
                icon = Icons.Rounded.RocketLaunch,
                title = "Sứ mệnh",
                description = "TeachFlow là nền tảng giáo dục thông minh thế hệ mới, giúp xóa bỏ rào cản giữa giáo viên và học sinh, tối ưu hóa quy trình quản lý và học tập một cách toàn diện.",
                primaryColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Team Section
            Text(
                text = "Đội ngũ phát triển",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                color = Color(0xFF1A1A2E)
            )
            
            DeveloperCard(
                name = "Nguyễn Công Đức",
                role = "Mobile Developer / Architect",
                icon = Icons.Rounded.Code,
                primaryColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            DeveloperCard(
                name = "Nguyễn Thị Bính Trâm",
                role = "Senior UI/UX Developer",
                icon = Icons.Rounded.AutoFixHigh,
                primaryColor = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Contact Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Liên hệ với chúng tôi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ContactRow(Icons.Rounded.Phone, "Hotline", "0334527953", primaryColor)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray.copy(alpha = 0.2f))
                    ContactRow(Icons.Rounded.Email, "Email", "support@teachflow.com", primaryColor)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray.copy(alpha = 0.2f))
                    ContactRow(Icons.Rounded.Language, "Website", "www.teachflow.com", primaryColor)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "© 2026 TeachFlow Corporation. All rights reserved.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AboutInfoCard(
    icon: ImageVector,
    title: String,
    description: String,
    primaryColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = primaryColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A2E))
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun DeveloperCard(
    name: String,
    role: String,
    icon: ImageVector,
    primaryColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(primaryColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = primaryColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF1A1A2E))
                Text(role, fontSize = 12.sp, color = primaryColor)
            }
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, label: String, value: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A2E))
        }
    }
}
