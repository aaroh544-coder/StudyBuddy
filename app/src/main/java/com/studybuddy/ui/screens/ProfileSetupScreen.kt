package com.studybuddy.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studybuddy.ui.components.GradientButton
import com.studybuddy.ui.components.SubjectChip
import com.studybuddy.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSetupScreen(
    onSetupComplete: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("") }
    var selectedSubjects by remember { mutableStateOf(setOf<String>()) }
    var selectedExam by remember { mutableStateOf("") }
    var dailyHours by remember { mutableFloatStateOf(3f) }
    
    val classes = listOf(
        "Class 6", "Class 7", "Class 8", "Class 9", "Class 10",
        "Class 11", "Class 12", "Undergraduate", "Postgraduate", "Other"
    )
    
    val subjects = listOf(
        "Mathematics", "Physics", "Chemistry", "Biology", "English",
        "Hindi", "Computer Science", "Economics", "History", "Geography",
        "Political Science", "Accountancy"
    )
    
    val exams = listOf(
        "Board Exams", "JEE Main", "JEE Advanced", "NEET", "CUET",
        "CAT", "GATE", "UPSC", "Other Competitive", "None / School Only"
    )
    
    val totalSteps = 4
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until totalSteps) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (i <= currentStep) Primary else SurfaceVariant
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Step ${currentStep + 1} of $totalSteps",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "step"
                ) { step ->
                    when (step) {
                        0 -> NameStep(name = name, onNameChange = { name = it })
                        1 -> ClassStep(
                            selectedClass = selectedClass,
                            classes = classes,
                            onClassSelect = { selectedClass = it }
                        )
                        2 -> SubjectsStep(
                            selectedSubjects = selectedSubjects,
                            subjects = subjects,
                            onSubjectToggle = { subject ->
                                selectedSubjects = if (subject in selectedSubjects) {
                                    selectedSubjects - subject
                                } else {
                                    selectedSubjects + subject
                                }
                            }
                        )
                        3 -> ExamStep(
                            selectedExam = selectedExam,
                            exams = exams,
                            onExamSelect = { selectedExam = it },
                            dailyHours = dailyHours,
                            onHoursChange = { dailyHours = it }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Back")
                    }
                }
                
                GradientButton(
                    text = if (currentStep == totalSteps - 1) "Complete" else "Next",
                    onClick = {
                        if (currentStep == totalSteps - 1) {
                            onSetupComplete()
                        } else {
                            currentStep++
                        }
                    },
                    modifier = Modifier.weight(if (currentStep > 0) 1f else 2f),
                    enabled = when (currentStep) {
                        0 -> name.isNotBlank()
                        1 -> selectedClass.isNotBlank()
                        2 -> selectedSubjects.isNotEmpty()
                        3 -> selectedExam.isNotBlank()
                        else -> true
                    }
                )
            }
        }
    }
}

@Composable
private fun NameStep(name: String, onNameChange: (String) -> Unit) {
    Column {
        Text(
            text = "What's your name?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Let's personalize your experience",
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Full Name") },
            placeholder = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                focusedLabelColor = Primary
            )
        )
    }
}

@Composable
private fun ClassStep(
    selectedClass: String,
    classes: List<String>,
    onClassSelect: (String) -> Unit
) {
    Column {
        Text(
            text = "Select your class",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "We'll match you with students at your level",
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        classes.forEach { classItem ->
            ClassRadioItem(
                text = classItem,
                isSelected = classItem == selectedClass,
                onClick = { onClassSelect(classItem) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ClassRadioItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryContainer else Surface
        ),
        border = if (isSelected) null else CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectsStep(
    selectedSubjects: Set<String>,
    subjects: List<String>,
    onSubjectToggle: (String) -> Unit
) {
    Column {
        Text(
            text = "Choose your subjects",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Select all subjects you're studying",
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            subjects.forEach { subject ->
                SubjectChip(
                    subject = subject,
                    isSelected = subject in selectedSubjects,
                    onClick = { onSubjectToggle(subject) }
                )
            }
        }
        
        if (selectedSubjects.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${selectedSubjects.size} subjects selected",
                style = MaterialTheme.typography.bodyMedium,
                color = Primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ExamStep(
    selectedExam: String,
    exams: List<String>,
    onExamSelect: (String) -> Unit,
    dailyHours: Float,
    onHoursChange: (Float) -> Unit
) {
    Column {
        Text(
            text = "Your exam goal",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "What exam are you preparing for?",
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        exams.forEach { exam ->
            ClassRadioItem(
                text = exam,
                isSelected = exam == selectedExam,
                onClick = { onExamSelect(exam) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Daily study goal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "${dailyHours.toInt()} hours per day",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        
        Slider(
            value = dailyHours,
            onValueChange = onHoursChange,
            valueRange = 1f..12f,
            steps = 10,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary
            )
        )
    }
}
