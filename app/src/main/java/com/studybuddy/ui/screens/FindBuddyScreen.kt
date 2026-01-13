package com.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studybuddy.data.model.ConnectionStatus
import com.studybuddy.data.model.MockStudyBuddies
import com.studybuddy.data.model.StudyBuddy
import com.studybuddy.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FindBuddyScreen(
    onBuddyClick: (String, String) -> Unit
) {
    var selectedClassFilter by remember { mutableStateOf<String?>(null) }
    var selectedSubjectFilter by remember { mutableStateOf<String?>(null) }
    var selectedExamFilter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var buddies by remember { mutableStateOf(MockStudyBuddies.buddies) }
    
    val classes = listOf("Class 10", "Class 11", "Class 12", "Undergraduate")
    val subjects = listOf("Physics", "Chemistry", "Mathematics", "Biology")
    val exams = listOf("JEE Main", "JEE Advanced", "NEET", "Board Exams")
    
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
                    text = "Find Study Buddy",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Connect with students preparing for the same goals",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name or subject...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }
        
        // Filters
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Filter by",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Class Filter
                FilterChip(
                    onClick = { selectedClassFilter = if (selectedClassFilter == null) classes.first() else null },
                    label = { Text(selectedClassFilter ?: "Class") },
                    selected = selectedClassFilter != null,
                    leadingIcon = if (selectedClassFilter != null) {
                        { Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(18.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )
                
                // Subject Filter
                FilterChip(
                    onClick = { selectedSubjectFilter = if (selectedSubjectFilter == null) subjects.first() else null },
                    label = { Text(selectedSubjectFilter ?: "Subject") },
                    selected = selectedSubjectFilter != null,
                    leadingIcon = if (selectedSubjectFilter != null) {
                        { Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(18.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )
                
                // Exam Filter
                FilterChip(
                    onClick = { selectedExamFilter = if (selectedExamFilter == null) exams.first() else null },
                    label = { Text(selectedExamFilter ?: "Exam") },
                    selected = selectedExamFilter != null,
                    leadingIcon = if (selectedExamFilter != null) {
                        { Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(18.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )
                
                // Clear Filters
                if (selectedClassFilter != null || selectedSubjectFilter != null || selectedExamFilter != null) {
                    FilterChip(
                        onClick = {
                            selectedClassFilter = null
                            selectedSubjectFilter = null
                            selectedExamFilter = null
                        },
                        label = { Text("Clear") },
                        selected = false,
                        leadingIcon = { Icon(Icons.Filled.Close, contentDescription = null, Modifier.size(18.dp)) }
                    )
                }
            }
        }
        
        HorizontalDivider(color = SurfaceVariant)
        
        // Results Count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${buddies.size} students found",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        
        // Buddy List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(buddies) { buddy ->
                BuddyListCard(
                    buddy = buddy,
                    onConnect = {
                        buddies = buddies.map {
                            if (it.id == buddy.id) {
                                it.copy(connectionStatus = ConnectionStatus.PENDING_SENT)
                            } else it
                        }
                    },
                    onClick = { onBuddyClick(buddy.id, buddy.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuddyListCard(
    buddy: StudyBuddy,
    onConnect: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(listOf(PrimaryLight, Primary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buddy.name.first().toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buddy.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${buddy.dailyStudyHours}h/day",
                            fontSize = 12.sp,
                            color = Secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextHint
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${buddy.classLevel} • ${buddy.examType}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Subjects
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    buddy.subjects.take(3).forEach { subject ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = subject,
                                fontSize = 11.sp,
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (buddy.subjects.size > 3) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${buddy.subjects.size - 3}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Connect Button
                when (buddy.connectionStatus) {
                    ConnectionStatus.NONE -> {
                        Button(
                            onClick = onConnect,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Connect")
                        }
                    }
                    ConnectionStatus.PENDING_SENT -> {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = false
                        ) {
                            Icon(
                                imageVector = Icons.Filled.HourglassTop,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Request Sent")
                        }
                    }
                    ConnectionStatus.CONNECTED -> {
                        Button(
                            onClick = onClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Message")
                        }
                    }
                    else -> { }
                }
            }
        }
    }
}
