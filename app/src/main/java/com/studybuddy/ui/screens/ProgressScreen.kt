package com.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.studybuddy.data.model.Badge
import com.studybuddy.data.model.BadgeDefinitions
import com.studybuddy.ui.components.SectionTitle
import com.studybuddy.ui.theme.*

@Composable
fun ProgressScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    // Demo data
    val totalStudyMinutes = 1250
    val currentStreak = 7
    val longestStreak = 14
    val weeklyData = listOf(45, 60, 30, 90, 75, 120, 80) // minutes per day
    val unlockedBadges = listOf("first_session", "streak_3", "streak_7", "hours_10", "pomodoro_10")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Your Progress",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Track your study journey",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProgressStatCard(
                        icon = Icons.Filled.Timer,
                        value = "${totalStudyMinutes / 60}h ${totalStudyMinutes % 60}m",
                        label = "Total Study",
                        modifier = Modifier.weight(1f)
                    )
                    
                    ProgressStatCard(
                        icon = Icons.Filled.LocalFireDepartment,
                        value = "$currentStreak days",
                        label = "Current Streak",
                        iconTint = StreakFire,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariant)
                    .padding(4.dp)
            ) {
                TabButton(
                    text = "Daily",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    text = "Weekly",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Weekly Chart
            SectionTitle(title = "This Week's Study Time")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val maxMinutes = weeklyData.maxOrNull() ?: 1
                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklyData.forEachIndexed { index, minutes ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${minutes}m",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height((120 * minutes / maxMinutes).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            if (index == 6) Primary 
                                            else Primary.copy(alpha = 0.5f + (index * 0.07f))
                                        )
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = days[index],
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    HorizontalDivider(color = SurfaceVariant)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Weekly Total",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "${weeklyData.sum() / 60}h ${weeklyData.sum() % 60}m",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Daily Average",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "${weeklyData.average().toInt()}m",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Streak Section
            SectionTitle(title = "Study Streak")
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = StreakFire,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$currentStreak",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = StreakFire
                            )
                        }
                        Text(
                            text = "Current Streak",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(60.dp)
                            .background(SurfaceVariant)
                    )
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = BadgeGold,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$longestStreak",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = BadgeGold
                            )
                        }
                        Text(
                            text = "Longest Streak",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Badges Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(title = "Badges")
                Text(
                    text = "${unlockedBadges.size}/${BadgeDefinitions.allBadges.size}",
                    fontSize = 14.sp,
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Badge Grid
            val badges = BadgeDefinitions.allBadges.map { badge ->
                badge.copy(isUnlocked = badge.id in unlockedBadges)
            }
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(240.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges.take(8)) { badge ->
                    BadgeItem(badge = badge)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProgressStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    iconTint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Primary else Color.Transparent,
            contentColor = if (isSelected) Color.White else TextSecondary
        ),
        elevation = null
    ) {
        Text(
            text = text,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
private fun BadgeItem(badge: Badge) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (badge.isUnlocked) {
                        Brush.horizontalGradient(listOf(BadgeGold, Secondary))
                    } else {
                        Brush.horizontalGradient(listOf(SurfaceVariant, SurfaceVariant))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (badge.iconName) {
                    "star" -> Icons.Filled.Star
                    "local_fire_department" -> Icons.Filled.LocalFireDepartment
                    "whatshot" -> Icons.Filled.Whatshot
                    "military_tech" -> Icons.Filled.MilitaryTech
                    "school" -> Icons.Filled.School
                    "psychology" -> Icons.Filled.Psychology
                    "emoji_events" -> Icons.Filled.EmojiEvents
                    "wb_sunny" -> Icons.Filled.WbSunny
                    "nightlight" -> Icons.Filled.Nightlight
                    "timer" -> Icons.Filled.Timer
                    "people" -> Icons.Filled.People
                    "groups" -> Icons.Filled.Groups
                    else -> Icons.Filled.Star
                },
                contentDescription = badge.name,
                tint = if (badge.isUnlocked) Color.White else TextHint,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = badge.name,
            fontSize = 10.sp,
            color = if (badge.isUnlocked) TextPrimary else TextHint,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
