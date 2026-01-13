package com.studybuddy.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studybuddy.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun TimerScreen() {
    var isStudyMode by remember { mutableStateOf(true) }
    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(25 * 60) } // 25 minutes in seconds
    var sessionsCompleted by remember { mutableIntStateOf(0) }
    
    val studyDuration = 25 * 60 // 25 minutes
    val breakDuration = 5 * 60 // 5 minutes
    
    val motivationalMessages = listOf(
        "You're doing great! Keep it up! 💪",
        "Stay focused, success is near! 🎯",
        "Every minute counts! ⏰",
        "You've got this! 🌟",
        "Hard work pays off! 📚"
    )
    
    var currentMessage by remember { mutableIntStateOf(0) }
    
    // Timer logic
    LaunchedEffect(isRunning) {
        while (isRunning && timeRemaining > 0) {
            delay(1000)
            timeRemaining--
            
            // Change motivational message every 5 minutes
            if (timeRemaining % 300 == 0) {
                currentMessage = (currentMessage + 1) % motivationalMessages.size
            }
        }
        
        if (timeRemaining == 0) {
            if (isStudyMode) {
                sessionsCompleted++
                isStudyMode = false
                timeRemaining = breakDuration
            } else {
                isStudyMode = true
                timeRemaining = studyDuration
            }
            isRunning = false
        }
    }
    
    val progress = if (isStudyMode) {
        timeRemaining.toFloat() / studyDuration
    } else {
        timeRemaining.toFloat() / breakDuration
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Focus Mode",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Mode indicator
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isStudyMode) TimerStudy else TimerBreak)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isStudyMode) "Study Time" else "Break Time",
                fontSize = 16.sp,
                color = if (isStudyMode) TimerStudy else TimerBreak,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Timer Circle
        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = SurfaceVariant,
                    radius = size.minDimension / 2,
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            // Progress Arc
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = if (isStudyMode) {
                            listOf(PrimaryLight, Primary, PrimaryDark)
                        } else {
                            listOf(TimerBreak, AccentGreen)
                        }
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            // Timer Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatTimer(timeRemaining),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isStudyMode) Primary else TimerBreak
                )
                
                Text(
                    text = if (isStudyMode) "remaining" else "break time",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Control Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            OutlinedButton(
                onClick = {
                    isRunning = false
                    timeRemaining = if (isStudyMode) studyDuration else breakDuration
                },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Play/Pause Button
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStudyMode) Primary else TimerBreak
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
            
            // Skip Button
            OutlinedButton(
                onClick = {
                    isRunning = false
                    if (isStudyMode) {
                        sessionsCompleted++
                        isStudyMode = false
                        timeRemaining = breakDuration
                    } else {
                        isStudyMode = true
                        timeRemaining = studyDuration
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SkipNext,
                    contentDescription = "Skip",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Sessions Completed
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
                    Text(
                        text = "$sessionsCompleted",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Text(
                        text = "Sessions",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(48.dp)
                        .background(SurfaceVariant)
                )
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${sessionsCompleted * 25}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                    Text(
                        text = "Minutes",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Motivational Message
        if (isRunning) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LightbulbCircle,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(32.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = motivationalMessages[currentMessage],
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Pomodoro Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pomodoro Technique",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "25 min study",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Coffee,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = TimerBreak
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "5 min break",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

private fun formatTimer(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}
