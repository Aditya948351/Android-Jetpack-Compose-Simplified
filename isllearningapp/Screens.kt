package com.example.isllearningapp

sealed class Screens (val screen: String) {
    object AboutUs : Screens("AboutUs")
    object AiChat : Screens("AiChat")
    object Exercise : Screens("Exercise")
    object Help : Screens("Help")
    object Home : Screens("Home")
    object Learn : Screens("Learn")
    object Practice : Screens("Practice")
    object Profile : Screens("Profile")
    object SignToSpeech : Screens("SignToSpeech")
    object Subjects : Screens("Subjects")
    object MyLearnings : Screens("MyLearnings")



}