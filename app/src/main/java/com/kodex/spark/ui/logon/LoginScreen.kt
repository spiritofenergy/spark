package com.kodex.spark.ui.logon

import android.R.attr.contentDescription
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.kodex.spark.R
import com.kodex.spark.ui.data.MainScreenDataObject
import kotlin.math.sign

@Composable
fun LoginScreen(
    onNavigationToMainScreen: (MainScreenDataObject) ->Unit
) {
    val successState = remember {
        mutableStateOf("Welcome")
    }
    val errorState = remember {
        mutableStateOf("")
    }
    val auth = remember {
        Firebase.auth
    }
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    //фон
    Image(
        painter = painterResource(
            id = R.drawable.way
        ),
        contentDescription = "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,

        )
        // Основной лист
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(46.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(250.dp).padding(bottom = 50.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "ИСКРА",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(40.dp))

        RoundedCornerTextField(
            text = emailState.value,
            label = "Email:"
        ) {
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password:"
        ) {
            passwordState.value = it
        }

        Spacer(modifier = Modifier.height(16.dp))
            if (errorState.value.isNotEmpty()){
                Text(
                    text = errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        LoginButton(
            text = "Sign in "
        ) {
            signIn(
                auth,
                emailState.value,
                passwordState.value,
                onSignInSuccess = { navData ->
                    onNavigationToMainScreen(navData)
                },
                onSignInFailure = { error ->
                    errorState.value = error
                    Log.d("MyTeg", "Sign Up Failure: $error")
                }
            )
            Log.d("MyTeg", "Press Sign In Button")
        }
        LoginButton(
            text = "Sign up "
        ) {
            signUp(
                auth,
                emailState.value,
                passwordState.value,
                onSignUpSuccess = {navData ->
                    onNavigationToMainScreen(navData)
                },
                onSignUpFailure = { error ->
                    Log.d("MyTeg", "Sign Up Failure: $error")
                }
            )
        }
    }
}

        //Connect
        fun signUp(
            auth: FirebaseAuth,
            email: String,
            password: String,
            onSignUpSuccess: (MainScreenDataObject) -> Unit,
            onSignUpFailure: (String) -> Unit
        ) {
            if (email.isBlank() || password.isBlank()) {
                onSignUpFailure("Email and Password be empty")
                return
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) onSignUpSuccess
                }
                .addOnFailureListener() {
                    onSignUpFailure(it.message ?: "Sign Up Error")
            }
        }

        fun signIn(
            auth: FirebaseAuth,
            email: String,
            password: String,
            onSignInSuccess: (MainScreenDataObject) -> Unit,
            onSignInFailure: (String) -> Unit
        ) {
            if (email.isBlank() || password.isBlank()) {
                onSignInFailure("Email and Password be empty")
                return
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        onSignInSuccess(MainScreenDataObject(
                            task.result.user?.uid!!,
                            task.result.user?.email!!
                    ))
                }
                .addOnFailureListener() {
                    onSignInFailure(it.message ?: "Sign Up Error")
            }
        }




/*

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}

*/
