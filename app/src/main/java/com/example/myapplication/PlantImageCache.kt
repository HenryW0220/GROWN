package com.example.myapplication

import androidx.compose.runtime.mutableStateMapOf

/**
 * Simple cache to store plant image paths.
 * Key = plant name (lowercase), Value = local file path
 * This avoids needing to modify SavedPlant or AppState.
 */
object PlantImageCache {
    val images = mutableStateMapOf<String, String>()

    fun getImagePath(plantName: String): String? {
        return images[plantName.lowercase().trim()]
    }

    fun saveImagePath(plantName: String, path: String) {
        images[plantName.lowercase().trim()] = path
    }
}
