package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.DarkText
import com.example.myapplication.ui.theme.GrayText
import com.example.myapplication.ui.theme.PageBackground
import com.example.myapplication.ui.theme.PlantGreen

// ==========================================
// Add Plant Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen(onNavigate: (String) -> Unit = {}) {
    // Form state
    var plantName by remember { mutableStateOf("") }
    var plantAge by remember { mutableStateOf("") }
    var plantNickname by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showPhotoDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            selectedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            selectedBitmap = it
        }
    }

    // Photo source selection dialog
    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Add Photo") },
            text = { Text("Choose a photo source") },
            confirmButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    cameraLauncher.launch(null)
                }) {
                    Text("Take Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Choose from Gallery")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Plant") },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Routes.DASHBOARD) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PageBackground
                )
            )
        },
        containerColor = PageBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Plant Name (required)
            Text(
                text = "Plant Name *",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = plantName,
                onValueChange = { plantName = it },
                placeholder = { Text("e.g. Aloe Vera, Monstera, Rose...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plant Age (optional)
            Text(
                text = "Plant Age (optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = plantAge,
                onValueChange = { plantAge = it },
                placeholder = { Text("e.g. 6 months, 2 years...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plant Nickname (optional)
            Text(
                text = "Nickname (optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = plantNickname,
                onValueChange = { plantNickname = it },
                placeholder = { Text("Give your plant a cute name...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Plant Photo (optional)
            Text(
                text = "Plant Photo (optional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Photo picker box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFFD5D5C0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(CardBackground)
                    .clickable { showPhotoDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (selectedBitmap != null) {
                    // Show selected photo
                    Image(
                        bitmap = selectedBitmap!!.asImageBitmap(),
                        contentDescription = "Plant photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // Overlay to re-select
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Change photo",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    // Empty state - show add icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Add photo",
                            tint = GrayText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap to add a photo",
                            fontSize = 14.sp,
                            color = GrayText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit button
            Button(
                onClick = {
                    if (plantName.isNotBlank()) {
                        // Save plant info to shared state and navigate to care result
                        PlantDataHolder.plantName = plantName
                        PlantDataHolder.plantAge = plantAge
                        PlantDataHolder.plantNickname = plantNickname
                        onNavigate(Routes.PLANT_CARE_RESULT)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PlantGreen,
                    disabledContainerColor = Color(0xFFB0B0B0)
                ),
                enabled = plantName.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Get AI Care Guide",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hint text
            Text(
                text = "AI will analyze your plant info and provide personalized care tips",
                fontSize = 12.sp,
                color = GrayText,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// Preview
// ==========================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddPlantScreenPreview() {
    MaterialTheme {
        AddPlantScreen()
    }
}
