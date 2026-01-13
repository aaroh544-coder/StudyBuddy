package com.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studybuddy.ui.components.GradientButton
import com.studybuddy.ui.components.SubjectChip
import com.studybuddy.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("Student") }
    var email by remember { mutableStateOf("student@email.com") }
    var phone by remember { mutableStateOf("+91 9876543210") }
    var selectedClass by remember { mutableStateOf("Class 12") }
    var selectedSubjects by remember { mutableStateOf(setOf("Physics", "Chemistry", "Mathematics")) }
    var selectedExam by remember { mutableStateOf("JEE Main") }
    var isSaving by remember { mutableStateOf(false) }
    
    val subjects = listOf(
        "Mathematics", "Physics", "Chemistry", "Biology", "English",
        "Hindi", "Computer Science", "Economics", "History", "Geography"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Edit Profile") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Surface
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Profile Picture Section
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(listOf(GradientStart, GradientEnd))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.first().toString(),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    // Edit icon
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Phone Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Class Dropdown
            Text(
                text = "Class / Education Level",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            var classExpanded by remember { mutableStateOf(false) }
            val classes = listOf(
                "Class 6", "Class 7", "Class 8", "Class 9", "Class 10",
                "Class 11", "Class 12", "Undergraduate", "Postgraduate"
            )
            
            ExposedDropdownMenuBox(
                expanded = classExpanded,
                onExpandedChange = { classExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedClass,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = classExpanded,
                    onDismissRequest = { classExpanded = false }
                ) {
                    classes.forEach { classItem ->
                        DropdownMenuItem(
                            text = { Text(classItem) },
                            onClick = {
                                selectedClass = classItem
                                classExpanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Subjects
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subject ->
                    SubjectChip(
                        subject = subject,
                        isSelected = subject in selectedSubjects,
                        onClick = {
                            selectedSubjects = if (subject in selectedSubjects) {
                                selectedSubjects - subject
                            } else {
                                selectedSubjects + subject
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Exam Type
            Text(
                text = "Exam Type",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            var examExpanded by remember { mutableStateOf(false) }
            val exams = listOf(
                "Board Exams", "JEE Main", "JEE Advanced", "NEET",
                "CUET", "CAT", "GATE", "Other"
            )
            
            ExposedDropdownMenuBox(
                expanded = examExpanded,
                onExpandedChange = { examExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedExam,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = examExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = examExpanded,
                    onDismissRequest = { examExpanded = false }
                ) {
                    exams.forEach { exam ->
                        DropdownMenuItem(
                            text = { Text(exam) },
                            onClick = {
                                selectedExam = exam
                                examExpanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Button
            GradientButton(
                text = if (isSaving) "Saving..." else "Save Changes",
                onClick = {
                    isSaving = true
                    // Simulate save and go back
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
