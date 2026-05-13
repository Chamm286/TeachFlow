package com.example.teachflow.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
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
                                Icons.Rounded.Notifications,
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
                        Icon(Icons.Rounded.Search, contentDescription = "Tìm kiếm", tint = textSecondaryColor)
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
                    NavItem("Trang chủ", Icons.Rounded.Home, Icons.Rounded.Home),
                    NavItem("Khám phá", Icons.Rounded.Explore, Icons.Rounded.Explore),
                    NavItem("Tính năng", Icons.Rounded.Widgets, Icons.Rounded.Widgets),
                    NavItem("Cá nhân", Icons.Rounded.PersonOutline, Icons.Rounded.Person),
                    NavItem("Khác", Icons.Rounded.MoreHoriz, Icons.Rounded.MoreHoriz)
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
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Hai nút Đăng nhập và Đăng ký bằng nhau
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { 
                                navController.navigate("welcome")
                                showSnackbar("🔐 Chuyển sang trang đăng nhập")
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = primaryColor
                            )
                        ) {
                            Text("Đăng nhập", fontWeight = FontWeight.Bold)
                        }
                        
                        // Nút Đăng ký với màu sắc hợp lý (Trắng mờ có viền)
                        OutlinedButton(
                            onClick = { 
                                navController.navigate("register")
                                showSnackbar("📝 Chuyển sang trang đăng ký")
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color.White),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White.copy(alpha = 0.15f),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Đăng ký", fontWeight = FontWeight.Bold)
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
                    icon = Icons.Rounded.PeopleAlt,
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
                    icon = Icons.Rounded.School,
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
                    icon = Icons.Rounded.StarRate,
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
                title = "Danh mục nhanh",
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
                        primaryColor = primaryColor,
                        onClick = { showSnackbar("📱 Đang chuyển đến ") }
                    )
                }
            }
        }
        
        // Bài viết nổi bật
        item {
            SectionHeader(
                title = "Bài viết nổi bật",
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
            PremiumIconBox(
                icon = icon,
                color = color,
                size = 48.dp,
                iconSize = 24.dp,
                shape = RoundedCornerShape(14.dp)
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
    val icon: ImageVector,
    val name: String,
    val color: Color,
    val route: String? = null
)

val quickCategories = listOf(
    QuickCategory(Icons.Rounded.SupervisedUserCircle, "Giáo viên", Color(0xFF4CAF50)),
    QuickCategory(Icons.Rounded.Face, "Học sinh", Color(0xFF2196F3)),
    QuickCategory(Icons.Rounded.Assessment, "Bảng điểm", Color(0xFFFF9800)),
    QuickCategory(Icons.Rounded.CalendarMonth, "Lịch học", Color(0xFFE91E63)),
    QuickCategory(Icons.Rounded.Forum, "Tin nhắn", Color(0xFF9C27B0)),
    QuickCategory(Icons.Rounded.FolderOpen, "Tài liệu", Color(0xFF00BCD4))
)

@Composable
fun QuickCategoryCard(
    category: QuickCategory,
    surfaceColor: Color,
    textPrimaryColor: Color,
    primaryColor: Color,
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
            PremiumIconBox(
                icon = category.icon,
                color = category.color,
                size = 48.dp,
                iconSize = 26.dp,
                shape = CircleShape
            )
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
            PremiumIconBox(
                icon = Icons.Rounded.Article,
                color = primaryColor,
                size = 52.dp,
                iconSize = 26.dp,
                shape = RoundedCornerShape(14.dp)
            )
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
                Icons.Rounded.ChevronRight,
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
            PremiumIconBox(
                icon = Icons.Rounded.Lightbulb,
                color = Color(0xFFFFC107),
                size = 48.dp,
                iconSize = 28.dp,
                shape = CircleShape
            )
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
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textPrimaryColor,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Cập nhật xu hướng giáo dục mới nhất",
                fontSize = 14.sp,
                color = textSecondaryColor
            )
        }
        
        item {
            SearchBarExplore(
                surfaceColor = surfaceColor,
                textHintColor = textHintColor,
                primaryColor = primaryColor,
                onSearch = { showSnackbar("🔍 Đang tìm kiếm: $it") }
            )
        }
        
        item {
            SectionHeader(
                title = "Gợi ý cho bạn",
                action = "Xem tất cả",
                onAction = { showSnackbar("📋 Xem thêm gợi ý") },
                textPrimaryColor = textPrimaryColor,
                primaryColor = primaryColor
            )
        }
        
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                exploreItems.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { item ->
                            ExploreGridCard(
                                item = item,
                                primaryColor = primaryColor,
                                surfaceColor = surfaceColor,
                                textPrimaryColor = textPrimaryColor,
                                textSecondaryColor = textSecondaryColor,
                                modifier = Modifier.weight(1f),
                                onClick = { showSnackbar("🚀 Đang mở: ${item.title}") }
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "Khóa học phổ biến",
                action = "Khám phá",
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
                onClick = { showSnackbar("📖 Đang mở khóa học: ${course.title}") }
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
            Icon(Icons.Rounded.Search, contentDescription = null, tint = textHintColor)
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
                    Icon(Icons.Rounded.ArrowForward, contentDescription = "Tìm kiếm", tint = primaryColor)
                }
            } else {
                Icon(Icons.Rounded.Tune, contentDescription = null, tint = textHintColor)
            }
        }
    }
}

data class ExploreItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

val exploreItems = listOf(
    ExploreItem("Lớp học AI", "Công nghệ giảng dạy 4.0", Icons.Rounded.AutoAwesome, Color(0xFF6366F1)),
    ExploreItem("Phân tích", "Theo dõi tiến độ học tập", Icons.Rounded.Insights, Color(0xFFEC4899)),
    ExploreItem("Tương tác", "Kết nối thầy cô & bạn bè", Icons.Rounded.Groups, Color(0xFF8B5CF6)),
    ExploreItem("Lịch trình", "Quản lý thời gian tối ưu", Icons.Rounded.EventAvailable, Color(0xFF10B981))
)

@Composable
fun ExploreGridCard(
    item: ExploreItem,
    primaryColor: Color,
    surfaceColor: Color,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PremiumIconBox(
                icon = item.icon,
                color = item.color,
                size = 52.dp,
                iconSize = 26.dp,
                shape = RoundedCornerShape(16.dp)
            )
            
            Column {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor,
                    lineHeight = 18.sp
                )
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = textSecondaryColor,
                    maxLines = 1
                )
            }
        }
    }
}

data class PopularCourse(
    val title: String,
    val students: Int,
    val rating: Double,
    val icon: ImageVector,
    val color: Color
)

val popularCourses = listOf(
    PopularCourse("Toán học Logic", 1240, 4.8, Icons.Rounded.Functions, Color(0xFF3F51B5)),
    PopularCourse("Lập trình Mobile", 980, 4.9, Icons.Rounded.Terminal, Color(0xFF4CAF50)),
    PopularCourse("Văn hóa Toàn cầu", 2100, 4.7, Icons.Rounded.Public, Color(0xFFE91E63)),
    PopularCourse("Kỹ năng Lãnh đạo", 560, 4.6, Icons.Rounded.MilitaryTech, Color(0xFFFF9800))
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
            PremiumIconBox(
                icon = course.icon,
                color = course.color,
                size = 56.dp,
                iconSize = 28.dp,
                shape = RoundedCornerShape(16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimaryColor
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Rounded.People, contentDescription = null, tint = textSecondaryColor, modifier = Modifier.size(14.dp))
                        Text("${course.students}", fontSize = 11.sp, color = textSecondaryColor)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Rounded.StarRate, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Text("${course.rating}", fontSize = 11.sp, color = Color(0xFFFFC107))
                    }
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
                text = "Tính năng nổi bật",
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
                title = "Thống kê cá nhân",
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
                title = "Cài đặt",
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
                onToggle = { showSnackbar("⚙️ Cài đặt đang được cập nhật") },
                onClick = { if (!setting.hasSwitch) showSnackbar("⚙️ Đang mở cài đặt") }
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

@Composable
fun PremiumIconBox(
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(14.dp)
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                spotColor = color.copy(alpha = 0.5f),
                ambientColor = color.copy(alpha = 0.2f)
            )
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(color, color.copy(alpha = 0.7f)),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.5f), Color.Transparent),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Hiệu ứng Inner Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent),
                        radius = 120f
                    ),
                    shape = shape
                )
        )
        
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}
