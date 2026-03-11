package com.kodex.spark.ui.logon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.spark.R
import com.kodex.spark.ui.custom.MyDialog
import com.kodex.spark.ui.data.MainScreenDataObject
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LViewModel = hiltViewModel(),
    onNavigationToMainScreen: (MainScreenDataObject) ->Unit = {}
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAccountState()
        viewModel.getEmail()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveLastEmail()
            viewModel.passwordState.value = ""
        }
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
    /*  Box(modifier = Modifier.fillMaxSize()
        .background(BoxFilter)
    )*/
    // Основной лист
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(46.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(250.dp).padding(bottom = 10.dp)

        )
        Text(
            text = "ИСКРА",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (viewModel.currentUser.value == null ) {
            RoundedCornerTextField(
                text = viewModel.emailState.value,
                label = "Логин:",
                isPassword = false
            ) {
                viewModel.emailState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (!viewModel.resetPasswordState.value) {
                RoundedCornerTextField(
                    text = viewModel.passwordState.value,
                    label = "Пароль:",
                    isPassword = true
                ) {
                    viewModel.passwordState.value = it
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            if (viewModel.errorState.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            if(!viewModel.resetPasswordState.value) {
                LoginButton(text = "Вход") {
                    viewModel.signIn(
                        onSignInSuccess = { navData ->
                            onNavigationToMainScreen(navData)

                        }
                    )
                }
            }
            LoginButton(
                text = if(viewModel.resetPasswordState.value) {
                "Восстановить пароль "
            }else {
                "Авторизация"
            },) {
                viewModel.signUp(
                    onSignUpSuccess = { navData ->
                        onNavigationToMainScreen(navData)
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
                if(!viewModel.resetPasswordState.value) {
                    Text(
                        modifier = Modifier.clickable {
                            viewModel.errorState.value = ""
                            viewModel.resetPasswordState.value = true
                        },
                        text = "Напомнить пароль",
                        color = Color.White
                    )
                }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
                LoginButton(
                    text = "Вход") {
                    onNavigationToMainScreen(
                        MainScreenDataObject(
                            viewModel.currentUser.value!!.uid,
                            viewModel.currentUser.value!!.email!!
                        )
                    )
                }
                LoginButton(text = "Выход") {
                    viewModel.signOut()
            }
        }
          MyDialog(
            showDialog = viewModel.showResetPasswordDialog.value,
            onDismiss = {
                viewModel.showResetPasswordDialog.value = false
            },
            onConfirm = {
                viewModel.showResetPasswordDialog.value = false
            },
            massage = stringResource(R.string.reset_password_massage)
        )
    }
}

@Composable
@Preview
fun ShowLoginScreen(){
    LoginScreen(

    )
}



