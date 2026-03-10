package com.example.myapplication

// Shared data holder for plant information between screens
object PlantDataHolder {
    var plantName: String = ""
    var plantAge: String = ""
    var plantNickname: String = ""

    // ID of the care guide to view in detail screen
    var selectedGuideId: Int = -1
}
