package com.example.teachflow.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teachflow.ui.settings.SettingsViewModel
import kotlinx.coroutines.launch

// Màu sắc light theme
val LightPrimary = Color(0xFF2196F3)
val LightAccent = Color(0xFF00BCD4)
val LightBackground = Color(0xFFF5F7FA)
val LightSurface = Color(0xFFFFFFFF)
val LightTextPrimary = Color(0xFF1A1A2E)
val LightTextSecondary = Color(0xFF666666)
val LightTextHint = Color(0xFF999999)

// Màu sắc dark theme
val DarkPrimary = Color(0xFF64B5F6)
val DarkAccent = Color(0xFF80DEEA)
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFB0B0B0)
val DarkTextHint = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(
    navController: NavController,
    viewModel: MainViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var selectedItem by remember { mutableStateOf(0) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showSnackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    val stats by viewModel.stats.collectAsState()
    val articles by viewModel.articles.collectAsState()
    val notificationCount by viewModel.notificationCount.collectAsState()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    
    // Lấy màu sắc non-null
    val primaryColor = if (isDarkTheme) DarkPrimary else LightPrimary
    val accentColor = if (isDarkTheme) DarkAccent else LightAccent
    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val textPrimaryColor = if (isDarkTheme) DarkTextPrimary else LightTextPrimary
    val textSecondaryColor = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
    val textHintColor = if (isDarkTheme) DarkTextHint else LightTextHint
    
    // Xử lý snackbar
    LaunchedEffect(showSnackbarMessage) {
        showSnackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            showSnackbarMessage = null
        }
    }
    
    fun showSnackbar(msg: String) {
        showSnackbarMessage = msg
    }
    
    Scaffold(
        modifier = Modifier.background(backgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Chào mừng trở lại! ✨",
                            fontSize = 13.sp,
                            color = textSecondaryColor
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "TeachFlow",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = primaryColor.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "2026",
                                    fontSize = 10.sp,
                                    color = primaryColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { 
                            showNotificationDialog = true
                            showSnackbar("📬 Bạn có  thông báo mới")
                        }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Thông báo",
                                tint = textSecondaryColor
                            )
                        }
                        if (notificationCount > 0) {
                            Badge(
                                containerColor = accentColor,
                                modifier = Modifier.offset(x = 8.dp, y = (-4).dp)
                            ) {
                                Text(
                                    text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    IconButton(onClick = { 
                        showSnackbar("🔍 Tính năng tìm kiếm đang phát triển")
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Tìm kiếm", tint = textSecondaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = surfaceColor,
                tonalElevation = 8.dp
            ) {
                val navItems = listOf(
                    NavItem("Trang chủ", Icons.Default.Home, Icons.Default.Home),
                    NavItem("Khám phá", Icons.Default.Explore, Icons.Default.Explore),
                    NavItem("Tính năng", Icons.Default.Apps, Icons.Default.Apps),
                    NavItem("Cá nhân", Icons.Default.Person, Icons.Default.Person),
                    NavItem("Khác", Icons.Default.MoreHoriz, Icons.Default.MoreHoriz)
                )
                
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { 
                            selectedItem = index
                            showSnackbar("📱 Đã chuyển sang ")
                        },
                        icon = {
                            Icon(
                                if (selectedItem == index) item.selectedIcon else item.icon,
                                contentDescription = item.title,
                                tint = if (selectedItem == index) primaryColor else textHintColor
                            )
                        },
                        label = {
                            Text(
                                item.title,
                                color = if (selectedItem == index) primaryColor else textHintColor,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = primaryColor,
                            selectedTextColor = primaryColor,
                            unselectedIconColor = textHintColor,
                            unselectedTextColor = textHintColor,
                            indicatorColor = primaryColor.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = backgroundColor
    ) { paddingValues ->
        when (selectedItem) {
            0 -> HomeTab(
                navController = navController,
                paddingValues = paddingValues,
                stats = stats,
                articles = articles,
                primaryColor = primaryColor,
                accentColor = accentColor,
                backgroundColor = backgroundColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                showSnackbar = ::showSnackbar
            )
            1 -> ExploreTab(
                navController = navController,
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                accentColor = accentColor,
                backgroundColor = backgroundColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                showSnackbar = ::showSnackbar
            )
            2 -> FeaturesTab(
                navController = navController,
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                accentColor = accentColor,
                backgroundColor = backgroundColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                showSnackbar = ::showSnackbar
            )
            3 -> ProfileTab(
                navController = navController,
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                accentColor = accentColor,
                backgroundColor = backgroundColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                showSnackbar = ::showSnackbar,
                settingsViewModel = settingsViewModel
            )
            4 -> MoreTab(
                navController = navController,
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                accentColor = accentColor,
                backgroundColor = backgroundColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                showSnackbar = ::showSnackbar
            )
        }
    }
    
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = { Text("📢 Thông báo", fontWeight = FontWeight.Bold, color = textPrimaryColor) },
            text = {
                Column {
                    Text("Bạn có  thông báo mới:", fontSize = 14.sp, color = textSecondaryColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("• Chào mừng đến với TeachFlow 2026", fontSize = 13.sp, color = textSecondaryColor)
                    Text("• Cập nhật tính năng Dark Mode", fontSize = 13.sp, color = textSecondaryColor)
                    Text("• Nhắc nhở kiểm tra điểm cuối kỳ", fontSize = 13.sp, color = textSecondaryColor)
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showNotificationDialog = false
                    showSnackbar("✅ Đã đọc thông báo")
                }) {
                    Text("Đã hiểu", color = primaryColor)
                }
            }
        )
    }
}

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun HomeTab(
    navController: NavController,
    paddingValues: PaddingValues,
    stats: StatsData,
    articles: List<ArticleData>,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    showSnackbar: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = primaryColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "✨ Chào mừng đến với",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "TeachFlow 2026",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Nền tảng quản lý giáo dục thông minh thế hệ mới",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { 
                                navController.navigate("welcome")
                                showSnackbar("🔐 Chuyển sang trang đăng nhập")
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = primaryColor
                            )
                        ) {
                            Text("Đăng nhập", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { 
                                navController.navigate("register")
                                showSnackbar("📝 Chuyển sang trang đăng ký")
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text("Đăng ký miễn phí")
                        }
                    }
                }
            }
        }
        
        // Thống kê nhanh
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Default.People,
                    value = formatNumber(stats.totalUsers),
                    label = "Người dùng",
                    color = primaryColor,
                    surfaceColor = surfaceColor,
                    textPrimaryColor = textPrimaryColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("👥 Hiện có  người dùng TeachFlow") }
                )
                StatCard(
                    icon = Icons.Default.School,
                    value = formatNumber(stats.totalClasses),
                    label = "Lớp học",
                    color = accentColor,
                    surfaceColor = surfaceColor,
                    textPrimaryColor = textPrimaryColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("📚 Đã có  lớp học trên hệ thống") }
                )
                StatCard(
                    icon = Icons.Default.Star,
                    value = "",
                    label = "Đánh giá",
                    color = Color(0xFFFFC107),
                    surfaceColor = surfaceColor,
                    textPrimaryColor = textPrimaryColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("⭐ Đánh giá trung bình /5 từ người dùng") }
                )
            }
        }
        
        // Danh mục nhanh
        item {
            SectionHeader(
                title = "🚀 Danh mục nhanh",
                action = "Xem tất cả",
                onAction = { showSnackbar("📋 Danh sách danh mục đang cập nhật") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(quickCategories) { category ->
                    QuickCategoryCard(
                        category = category,
                        surfaceColor = surfaceColor,
                        textPrimaryColor = textPrimaryColor,
                        onClick = { showSnackbar("📱 Đang chuyển đến ") }
                    )
                }
            }
        }
        
        // Bài viết nổi bật
        item {
            SectionHeader(
                title = "📝 Bài viết nổi bật",
                action = "Xem thêm",
                onAction = { showSnackbar("📖 Danh sách bài viết đang cập nhật") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        items(articles.take(3)) { article ->
            FeaturedArticleCard(
                article = article,
                primaryColor = primaryColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                onClick = { showSnackbar("📄 Đang đọc: ") }
            )
        }
        
        // Mẹo hữu ích
        item {
            TipOfTheDay(
                primaryColor = primaryColor,
                textSecondaryColor = textSecondaryColor,
                onClick = { showSnackbar("💡 Mẹo: Học Pomodoro giúp tăng 40% hiệu suất!") }
            )
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = textSecondaryColor
            )
        }
    }
}

data class QuickCategory(
    val icon: String,
    val name: String,
    val route: String? = null
)

val quickCategories = listOf(
    QuickCategory("👨‍🏫", "Giáo viên"),
    QuickCategory("👨‍🎓", "Học sinh"),
    QuickCategory("📊", "Bảng điểm"),
    QuickCategory("📅", "Lịch học"),
    QuickCategory("💬", "Tin nhắn"),
    QuickCategory("📁", "Tài liệu")
)

@Composable
fun QuickCategoryCard(
    category: QuickCategory,
    surfaceColor: Color,
    textPrimaryColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(category.icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimaryColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FeaturedArticleCard(
    article: ArticleData,
    primaryColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(12.dp),
                color = primaryColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📄", fontSize = 24.sp)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor,
                    maxLines = 1
                )
                Text(
                    text = article.description.take(60) + "...",
                    fontSize = 12.sp,
                    color = textSecondaryColor,
                    maxLines = 2
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = article.date,
                        fontSize = 10.sp,
                        color = textHintColor
                    )
                    Text(
                        text = article.readTime,
                        fontSize = 10.sp,
                        color = textHintColor
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textHintColor
            )
        }
    }
}

@Composable
fun TipOfTheDay(
    primaryColor: Color,
    textSecondaryColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryColor.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💡", fontSize = 32.sp)
            Column {
                Text(
                    text = "Mẹo hôm nay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Text(
                    text = "Học 25 phút, nghỉ 5 phút - Phương pháp Pomodoro giúp tăng 40% hiệu suất!",
                    fontSize = 12.sp,
                    color = textSecondaryColor,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    action: String?,
    onAction: () -> Unit,
    textPrimaryColor: Color,
    primaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimaryColor
        )
        if (action != null) {
            Text(
                text = action,
                fontSize = 13.sp,
                color = primaryColor,
                modifier = Modifier.clickable { onAction() }
            )
        }
    }
}

fun formatNumber(num: Int): String {
    return when {
        num >= 1000000 -> "M"
        num >= 1000 -> "K"
        else -> num.toString()
    }
}

// ==================== CÁC TAB KHÁC ====================
@Composable
fun ExploreTab(
    navController: NavController,
    paddingValues: PaddingValues,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    showSnackbar: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "✨ Khám phá",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
            Text(
                text = "Tính năng và dịch vụ mới nhất",
                fontSize = 14.sp,
                color = textSecondaryColor
            )
        }
        
        item {
            SearchBarExplore(
                surfaceColor = surfaceColor,
                textHintColor = textHintColor,
                primaryColor = primaryColor,
                onSearch = { showSnackbar("🔍 Đang tìm kiếm: ") }
            )
        }
        
        item {
            SectionHeader(
                title = "🎯 Gợi ý cho bạn",
                action = "Xem thêm",
                onAction = { showSnackbar("📋 Xem thêm gợi ý") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        items(exploreItems) { item ->
            ExploreCard(
                item = item,
                primaryColor = primaryColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                onClick = { showSnackbar("🚀 Đang mở: ") }
            )
        }
        
        item {
            SectionHeader(
                title = "🏆 Khóa học phổ biến",
                action = "Xem tất cả",
                onAction = { showSnackbar("📚 Danh sách khóa học") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        items(popularCourses) { course ->
            CourseCard(
                course = course,
                primaryColor = primaryColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                onClick = { showSnackbar("📖 Đang mở khóa học: ") }
            )
        }
    }
}

@Composable
fun SearchBarExplore(
    surfaceColor: Color,
    textHintColor: Color,
    primaryColor: Color,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = surfaceColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = textHintColor)
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Tìm kiếm khóa học, bài viết...", color = textHintColor) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { 
                    onSearch(searchText)
                    searchText = ""
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Tìm kiếm", tint = primaryColor)
                }
            } else {
                Icon(Icons.Default.Tune, contentDescription = null, tint = textHintColor)
            }
        }
    }
}

data class ExploreItem(
    val title: String,
    val description: String,
    val icon: String
)

val exploreItems = listOf(
    ExploreItem("Lớp học thông minh", "Quản lý lớp học với công nghệ AI", "🤖"),
    ExploreItem("Bảng điểm tự động", "Nhập điểm và tính toán tự động", "📊"),
    ExploreItem("Thống kê chi tiết", "Biểu đồ phân tích kết quả học tập", "📈"),
    ExploreItem("Thông báo tức thì", "Gửi thông báo đến phụ huynh", "🔔")
)

@Composable
fun ExploreCard(
    item: ExploreItem,
    primaryColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = primaryColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.icon, fontSize = 24.sp)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = textSecondaryColor
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = textHintColor
            )
        }
    }
}

data class PopularCourse(
    val title: String,
    val students: Int,
    val rating: Double,
    val icon: String
)

val popularCourses = listOf(
    PopularCourse("Toán cao cấp", 1240, 4.8, "📐"),
    PopularCourse("Lập trình Android", 980, 4.9, "📱"),
    PopularCourse("Tiếng Anh giao tiếp", 2100, 4.7, "🇬🇧"),
    PopularCourse("Kỹ năng mềm", 560, 4.6, "💼")
)

@Composable
fun CourseCard(
    course: PopularCourse,
    primaryColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(course.icon, fontSize = 32.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("👥 ", fontSize = 11.sp, color = textSecondaryColor)
                    Text("⭐ ", fontSize = 11.sp, color = Color(0xFFFFC107))
                }
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = primaryColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "Chi tiết",
                    fontSize = 12.sp,
                    color = primaryColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// ==================== TAB 3: TÍNH NĂNG ====================
@Composable
fun FeaturesTab(
    navController: NavController,
    paddingValues: PaddingValues,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    showSnackbar: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "⚡ Tính năng nổi bật",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
            Text(
                text = "Trải nghiệm đầy đủ tính năng của TeachFlow",
                fontSize = 14.sp,
                color = textSecondaryColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        items(featuresList) { feature ->
            FeatureCard(
                feature = feature,
                primaryColor = primaryColor,
                accentColor = accentColor,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                onClick = { 
                    if (feature.isPremium) {
                        showSnackbar("⭐ Tính năng Premium:  - Vui lòng nâng cấp tài khoản")
                    } else if (feature.isNew) {
                        showSnackbar("✨ Tính năng mới:  - Đang trong giai đoạn thử nghiệm")
                    } else {
                        showSnackbar("🚀 Đang mở: ")
                    }
                }
            )
        }
    }
}

data class FeatureItem(
    val icon: String,
    val title: String,
    val description: String,
    val isPremium: Boolean = false,
    val isNew: Boolean = false
)

val featuresList = listOf(
    FeatureItem("👨‍🏫", "Quản lý lớp học", "Tạo, chỉnh sửa và quản lý lớp học dễ dàng"),
    FeatureItem("📊", "Bảng điểm thông minh", "Nhập điểm, tính điểm trung bình tự động"),
    FeatureItem("📈", "Thống kê chi tiết", "Biểu đồ phân tích kết quả học tập"),
    FeatureItem("🔔", "Thông báo", "Gửi thông báo đến học sinh và phụ huynh"),
    FeatureItem("💬", "Chat trực tuyến", "Trao đổi giữa giáo viên và học sinh", true),
    FeatureItem("📁", "Kho tài liệu", "Tải lên và chia sẻ tài liệu", true),
    FeatureItem("🏆", "Bảng xếp hạng", "Xếp hạng thành tích học tập", false, true),
    FeatureItem("🎓", "Chứng chỉ", "Tạo và cấp chứng chỉ cho học sinh", true)
)

@Composable
fun FeatureCard(
    feature: FeatureItem,
    primaryColor: Color,
    accentColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(feature.icon, fontSize = 36.sp)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = feature.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimaryColor
                    )
                    if (feature.isNew) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = accentColor.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Mới",
                                fontSize = 9.sp,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (feature.isPremium) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF9800).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Premium",
                                fontSize = 9.sp,
                                color = Color(0xFFFF9800),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = feature.description,
                    fontSize = 12.sp,
                    color = textSecondaryColor
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textHintColor
            )
        }
    }
}

// ==================== TAB 4: CÁ NHÂN ====================
@Composable
fun ProfileTab(
    navController: NavController,
    paddingValues: PaddingValues,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    showSnackbar: (String) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = surfaceColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = primaryColor.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("👤", fontSize = 44.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Khách",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimaryColor
                    )
                    Text(
                        text = "Đăng nhập để trải nghiệm đầy đủ",
                        fontSize = 13.sp,
                        color = textSecondaryColor
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { 
                            navController.navigate("welcome")
                            showSnackbar("🔐 Chuyển sang trang đăng nhập")
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("ĐĂNG NHẬP", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        item {
            SectionHeader(
                title = "📊 Thống kê cá nhân",
                action = "Chi tiết",
                onAction = { showSnackbar("📈 Thống kê chi tiết đang cập nhật") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PersonalStatCard(
                    value = "0",
                    label = "Lớp tham gia",
                    primaryColor = primaryColor,
                    surfaceColor = surfaceColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("📚 Bạn chưa tham gia lớp học nào") }
                )
                PersonalStatCard(
                    value = "0",
                    label = "Bài tập",
                    primaryColor = primaryColor,
                    surfaceColor = surfaceColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("📝 Bạn chưa có bài tập nào") }
                )
                PersonalStatCard(
                    value = "0",
                    label = "Điểm TB",
                    primaryColor = primaryColor,
                    surfaceColor = surfaceColor,
                    textSecondaryColor = textSecondaryColor,
                    modifier = Modifier.weight(1f),
                    onClick = { showSnackbar("📊 Điểm trung bình hiện tại: 0") }
                )
            }
        }
        
        item {
            SectionHeader(
                title = "⚙️ Cài đặt",
                action = null,
                onAction = {},
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        item {
            // Dark mode switch
            SettingsCard(
                icon = if (isDarkTheme) "🌙" else "☀️",
                title = "Chế độ tối",
                hasSwitch = true,
                isOn = isDarkTheme,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor,
                textHintColor = textHintColor,
                onToggle = { 
                    settingsViewModel.toggleTheme()
                    showSnackbar(if (isDarkTheme) "🌙 Đã chuyển sang chế độ sáng" else "☀️ Đã chuyển sang chế độ tối")
                },
                onClick = { }
            )
        }
        
        items(settingsList) { setting ->
            SettingsCard(
                icon = setting.icon,
                title = setting.title,
                hasSwitch = setting.hasSwitch,
                isOn = setting.isOn,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor,
                textHintColor = textHintColor,
                onToggle = { showSnackbar("⚙️ Cài đặt  đang được cập nhật") },
                onClick = { if (!setting.hasSwitch) showSnackbar("⚙️ Đang mở cài đặt ") }
            )
        }
    }
}

@Composable
fun PersonalStatCard(
    value: String,
    label: String,
    primaryColor: Color,
    surfaceColor: Color,
    textSecondaryColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = textSecondaryColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class SettingItem(
    val icon: String,
    val title: String,
    val hasSwitch: Boolean = false,
    val isOn: Boolean = false
)

val settingsList = listOf(
    SettingItem("🔔", "Thông báo", true, true),
    SettingItem("🌐", "Ngôn ngữ"),
    SettingItem("🎨", "Chủ đề"),
    SettingItem("🔒", "Bảo mật"),
    SettingItem("ℹ️", "Thông tin ứng dụng")
)

@Composable
fun SettingsCard(
    icon: String,
    title: String,
    surfaceColor: Color,
    textPrimaryColor: Color,
    primaryColor: Color,
    textHintColor: Color,
    hasSwitch: Boolean = false,
    isOn: Boolean = false,
    onToggle: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!hasSwitch) onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(icon, fontSize = 22.sp)
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = textPrimaryColor
                )
            }
            if (hasSwitch) {
                Switch(
                    checked = isOn,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = primaryColor,
                        uncheckedThumbColor = textHintColor,
                        uncheckedTrackColor = textHintColor.copy(alpha = 0.3f)
                    )
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = textHintColor
                )
            }
        }
    }
}

// ==================== TAB 5: KHÁC ====================
@Composable
fun MoreTab(
    navController: NavController,
    paddingValues: PaddingValues,
    primaryColor: Color,
    accentColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    showSnackbar: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "📌 Tiện ích",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(utilities) { utility ->
            UtilityCard(
                utility = utility,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                navController = navController,
                showSnackbar = showSnackbar
            )
        }
        
        item {
            Text(
                text = "❤️ Hỗ trợ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(supports) { support ->
            UtilityCard(
                utility = support,
                surfaceColor = surfaceColor,
                textPrimaryColor = textPrimaryColor,
                textSecondaryColor = textSecondaryColor,
                textHintColor = textHintColor,
                navController = navController,
                showSnackbar = showSnackbar
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "TeachFlow v2026.1.0",
                fontSize = 12.sp,
                color = textHintColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "© 2026 TeachFlow Team",
                fontSize = 11.sp,
                color = textHintColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

data class UtilityItem(
    val icon: String,
    val title: String,
    val description: String,
    val route: String? = null
)

val utilities = listOf(
    UtilityItem("📱", "Chia sẻ ứng dụng", "Giới thiệu TeachFlow cho bạn bè"),
    UtilityItem("⭐", "Đánh giá ứng dụng", "Đánh giá 5 sao để ủng hộ chúng tôi"),
    UtilityItem("ℹ️", "Giới thiệu", "Thông tin về TeachFlow", "about"),
    UtilityItem("📞", "Liên hệ", "Hotline: 1900 1234")
)

val supports = listOf(
    UtilityItem("❓", "Trợ giúp", "Hướng dẫn sử dụng"),
    UtilityItem("💬", "Phản hồi", "Góp ý và báo lỗi"),
    UtilityItem("🔒", "Chính sách bảo mật", "Điều khoản sử dụng")
)

@Composable
fun UtilityCard(
    utility: UtilityItem,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    textHintColor: Color,
    navController: NavController,
    showSnackbar: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                when {
                    utility.route != null -> {
                        navController.navigate(utility.route)
                        showSnackbar("📱 Đang mở: ")
                    }
                    utility.title == "Chia sẻ ứng dụng" -> {
                        showSnackbar("📱 Tính năng chia sẻ đang phát triển")
                    }
                    utility.title == "Đánh giá ứng dụng" -> {
                        showSnackbar("⭐ Cảm ơn bạn đã đánh giá TeachFlow!")
                    }
                    else -> {
                        showSnackbar("🚀 : ")
                    }
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(utility.icon, fontSize = 28.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = utility.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimaryColor
                )
                Text(
                    text = utility.description,
                    fontSize = 12.sp,
                    color = textSecondaryColor
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textHintColor
            )
        }
    }
}
