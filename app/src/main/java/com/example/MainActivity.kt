package com.example



import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Cyan400
import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.data.SoloDatabase
import com.example.data.SoloRepository
import com.example.managers.FitnessTrackerManager
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.StatusScreen
import com.example.ui.screens.SettingsScreen
import com.example.managers.LocationTrackerManager
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SoloViewModel
import com.example.viewmodel.SoloViewModelFactory
import kotlinx.serialization.Serializable
import com.example.ui.theme.Cyan500
import com.example.ui.theme.AppBackground
import com.example.ui.theme.TextSecondary
import androidx.compose.ui.graphics.Color
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.animation.core.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Info

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Shadow
import com.example.ui.theme.Cyan300

@Serializable
object StatusRoute

@Serializable
object QuestsRoute

@Serializable
object SkillsRoute

@Serializable
object SettingsRoute

@Serializable
object LeaderboardRoute

@Serializable
object DashboardRoute

class MainActivity : ComponentActivity() {

    private lateinit var db: SoloDatabase
    private lateinit var repository: SoloRepository
    private lateinit var fitnessTrackerManager: FitnessTrackerManager
    private lateinit var locationTrackerManager: LocationTrackerManager
    private lateinit var healthConnectManager: com.example.managers.HealthConnectManager
    
    private val viewModel: SoloViewModel by viewModels {
        SoloViewModelFactory(repository, fitnessTrackerManager, locationTrackerManager, healthConnectManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        db = Room.databaseBuilder(applicationContext, SoloDatabase::class.java, "solo_db")
            .fallbackToDestructiveMigration()
            .build()
        repository = SoloRepository(db.soloDao())
        fitnessTrackerManager = FitnessTrackerManager(this)
        locationTrackerManager = LocationTrackerManager(this)
        healthConnectManager = com.example.managers.HealthConnectManager(this)
        
        
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 100)
        }
        com.example.workers.WorkManagerUtils.scheduleEveningReminder(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel)
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.startTracking()
    }
    
    override fun onPause() {
        super.onPause()
        viewModel.stopTracking()
    }
}

@Composable
fun MainAppScreen(viewModel: SoloViewModel) {
    val navController = rememberNavController()
    
    val userStats by viewModel.userStats.collectAsState()
    val quests by viewModel.allQuests.collectAsState()
    val skills by viewModel.allSkills.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val showLevelUp by viewModel.showLevelUp.collectAsState()

    if (showLevelUp) {
        LevelUpModal(level = userStats?.level ?: 1) {
            viewModel.dismissLevelUp()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black.copy(alpha = 0.8f),
                contentColor = Cyan500,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                NavigationBarItem(
                    selected = currentRoute == StatusRoute::class.qualifiedName,
                    onClick = { navController.navigate(StatusRoute) },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Status") },
                    label = { Text("STATUS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
                
                NavigationBarItem(
                    selected = currentRoute == QuestsRoute::class.qualifiedName,
                    onClick = { navController.navigate(QuestsRoute) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Quests") },
                    label = { Text("QUESTS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
                
                NavigationBarItem(
                    selected = currentRoute == SkillsRoute::class.qualifiedName,
                    onClick = { navController.navigate(SkillsRoute) },
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Skills") },
                    label = { Text("SKILLS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
                
                NavigationBarItem(
                    selected = currentRoute == SettingsRoute::class.qualifiedName,
                    onClick = { navController.navigate(SettingsRoute) },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("SETTINGS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
                
                NavigationBarItem(
                    selected = currentRoute == LeaderboardRoute::class.qualifiedName,
                    onClick = { navController.navigate(LeaderboardRoute) },
                    icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = "Rank") },
                    label = { Text("RANK") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
                
                NavigationBarItem(
                    selected = currentRoute == DashboardRoute::class.qualifiedName,
                    onClick = { navController.navigate(DashboardRoute) },
                    icon = { Icon(Icons.Filled.ShowChart, contentDescription = "Dashboard") },
                    label = { Text("STATS") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppBackground,
                        selectedTextColor = Cyan500,
                        indicatorColor = Cyan500,
                        unselectedIconColor = TextSecondary.copy(alpha = 0.5f),
                        unselectedTextColor = TextSecondary.copy(alpha = 0.5f)
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = StatusRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<StatusRoute> {
                val heartRate by viewModel.heartRate.collectAsState()
                val statMilestone by viewModel.statMilestone.collectAsState()
                StatusScreen(
                    stats = userStats,
                    steps = steps,
                    onAddStat = { viewModel.addStatPoint(it) },
                    statMilestone = statMilestone,
                    onDismissMilestone = { viewModel.dismissStatMilestone() }
                )
            }
            composable<QuestsRoute> {
                val distance by viewModel.distance.collectAsState()
                QuestsScreen(quests = quests, distanceMeters = distance, onProgressQuest = { quest ->
                    viewModel.progressQuest(quest, 10)
                }, onGenerateRandomQuest = {
                    viewModel.generateNewRandomQuest()
                }, onGenerateBossRaid = {
                    viewModel.generateWeeklyBossRaid()
                }, onGenerateDailies = {
                    viewModel.generateDailyQuests()
                })
            }
            composable<SkillsRoute> {
                SkillsScreen(skills = skills, currentLevel = userStats?.level ?: 1)
            }
            composable<SettingsRoute> {
                SettingsScreen(stats = userStats, onSaveSettings = { pushUps, squats, plank, sitUps, distance, condition ->
                    viewModel.saveSettings(pushUps, squats, plank, sitUps, distance, condition)
                })
            }
            composable<LeaderboardRoute> {
                val leaderboard by viewModel.leaderboard.collectAsState()
                com.example.ui.screens.LeaderboardScreen(entries = leaderboard)
            }
            composable<DashboardRoute> {
                com.example.ui.screens.DashboardScreen()
            }
        }
    }
}


@Composable
fun LevelUpModal(level: Int, onDismiss: () -> Unit) {
    val SysDarkBlue = Color(0xFF09121E)
    val SysBorderBlue = Color(0xFF4CB5F9)
    val SysTextBlue = Color(0xFFA0E4FF)
    val SysBorderInner = Color(0x664CB5F9)
    
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Dialog(onDismissRequest = onDismiss, properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99040B13)) // Deep space background slightly transparent
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(SysDarkBlue.copy(alpha = 0.95f))
                    .border(2.dp, SysBorderBlue.copy(alpha = glowAlpha))
                    .padding(2.dp)
                    .border(1.dp, SysBorderInner)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Notification Title Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SysBorderInner)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = SysTextBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "NOTIFICATION",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 4.sp,
                                style = androidx.compose.ui.text.TextStyle(
                                    shadow = androidx.compose.ui.graphics.Shadow(
                                        color = SysTextBlue,
                                        blurRadius = 8f
                                    )
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "You have leveled up!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Level Reached: $level",
                        color = SysTextBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = SysTextBlue,
                                blurRadius = 12f
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .border(1.dp, SysBorderInner)
                                .clickable { onDismiss() }
                                .padding(horizontal = 32.dp, vertical = 12.dp)
                        ) {
                            Text("Confirm", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
