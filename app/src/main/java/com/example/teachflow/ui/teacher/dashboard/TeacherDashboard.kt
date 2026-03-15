package com.example.teachflow.ui.teacher.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.teachflow.ui.components.StatCard
import com.example.teachflow.ui.components.ClassCard
import com.example.teachflow.ui.theme.Primary
import com.example.teachflow.ui.theme.StudentColor
import com.example.teachflow.ui.theme.TeacherColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboard(
    teacherId: String,
    onLogout: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToGrade: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .width(300.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Primary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.White)
                        ) {
                            Text(
                                text = "👨‍🏫",
                                fontSize = 40.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nguyễn Văn An",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Giáo viên Toán",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Tổng quan") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Class, contentDescription = null) },
                    label = { Text("Lớp học") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToClasses()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Grade, contentDescription = null) },
                    label = { Text("Nhập điểm") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToGrade()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Thống kê") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToStatistics()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    label = { Text("Đăng xuất") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TeachFlow") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Thông báo")
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Primary.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Xin chào, 👋",
                                    fontSize = 14.sp,
                                    color = Primary
                                )
                                Text(
                                    text = "Nguyễn Văn An",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                Icons.Default.WavingHand,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = Icons.Default.Class,
                            title = "Lớp học",
                            value = "3",
                            color = TeacherColor,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.People,
                            title = "Học sinh",
                            value = "98",
                            color = StudentColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Text(
                        text = "Lớp học gần đây",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(
                    listOf(
                        Triple("10A1", "Toán", "35 HS"),
                        Triple("10A2", "Toán", "38 HS"),
                        Triple("11B1", "Toán", "32 HS")
                    )
                ) { (className, subject, count) ->
                    ClassCard(
                        className = className,
                        subject = subject,
                        studentCount = count,
                        onClick = onNavigateToClasses
                    )
                }
            }
        }
    }
}
