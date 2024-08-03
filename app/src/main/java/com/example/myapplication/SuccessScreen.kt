package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SuccessScreen(navController: NavController) {
    val byColor = Color(0xFFECFADC)
    val tColor = Color(0xFF2B472B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(byColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.sapo),
            contentDescription = null,
            modifier = Modifier.size(500.dp)
        )
        Button(
            onClick = {
                Firebase.auth.signOut()
                navController.navigate("home")
            }
        ) {
            Text(text = "Sair")
        }
    }
}