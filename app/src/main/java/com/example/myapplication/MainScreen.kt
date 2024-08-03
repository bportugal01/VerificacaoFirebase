package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("otp") {
            OtpScreen(navController = navController)
        }
        composable("success") {
            SuccessScreen(navController = navController)
        }
    }
}


val auth = FirebaseAuth.getInstance()

var storedVerificationId = ""

fun signInWithPhoneAuthCredential(context: Context, credential:PhoneAuthCredential, navController: NavController) {

    auth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { taks ->
            if (taks.isSuccessful) {
                Toast.makeText(context, "Login feito com Sucesso", Toast.LENGTH_LONG).show()
                navController.navigate("success")
                val user = taks.result?.user
            } else {
                if (taks.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Erro ao realizar o login", Toast.LENGTH_LONG).show()
                }
            }
        }
}

fun onLoginClicked(
    context: Context, navController: NavController, phoneNumber: String, onCodeSend: () -> Unit
) {
    auth.setLanguageCode("pt-br")
    val callback= object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.d("Lista Telefônica","Varificação Concluída")
            signInWithPhoneAuthCredential(context, p0, navController)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("Lista Telefônica","Falha na Verificação$p0")
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Log.d("Lista Telefônica", "Código Enviado$p0")
            storedVerificationId = p0
            onCodeSend()
        }

    }

    val option = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber("+55$phoneNumber")
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as Activity)
        .setCallbacks(callback)
        .build()
    PhoneAuthProvider.verifyPhoneNumber(option)

}

fun verifyPhoneNumberWithCode(
    context: Context, verificationId: String, code: String, navController: NavController
){
    val p0 = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(context, p0, navController)
}
