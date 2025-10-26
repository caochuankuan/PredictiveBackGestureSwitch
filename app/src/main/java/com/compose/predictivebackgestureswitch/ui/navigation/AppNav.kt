package com.compose.predictivebackgestureswitch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.predictivebackgestureswitch.ui.components.MainContent
import com.compose.predictivebackgestureswitch.ui.components.BlankScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainContent(navController)
        }
        composable("blank") {
            BlankScreen()
        }
    }
}