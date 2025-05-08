package com.kodex.spark.ui.logon

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.utils.AuthManager
import com.kodex.spark.ui.utils.Categories
import com.kodex.spark.ui.utils.firebase.StoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager,
) : ViewModel() {
    val selectedCategory = mutableIntStateOf(Categories.FANTASY)
    val currentUser = mutableStateOf<FirebaseUser?>(null)
    val showResetPasswordDialog = mutableStateOf(false)
    val successState = mutableStateOf("Welcome")
    val errorState = mutableStateOf("")
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("test24")
    val resetPasswordState = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (MainScreenDataObject) -> Unit,
    ) {
        errorState.value = ""
        authManager.signIn(
            emailState.value,
            passwordState.value,
            onSignInSuccess = { navData ->
                onSignInSuccess(navData)
            },
            onSignInFailure = { errorMassage ->
                errorState.value = errorMassage
            }
        )
    }
    fun getEmail(){
        emailState.value = storeManager.getString(StoreManager.EMAIL_KEY, "")
    }

    fun saveLastEmail(){
        storeManager.saveString(StoreManager.EMAIL_KEY, emailState.value)
    }

    fun signUp(
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
    ) {
        errorState.value = ""
        if (resetPasswordState.value) {
            authManager.resetPassword(
                emailState.value,
                onResetPasswordSuccess = {
                    resetPasswordState.value = false
                    showResetPasswordDialog.value = true
                    Log.d("MyLog", "SendPass_1")
                },
                onResetPasswordFailure = { errorMessage ->
                    errorState.value = errorMessage
                }
            )
            return
        }
        authManager.signUp(
            emailState.value,
            passwordState.value,
            onSignUpSuccess = { navData ->
                onSignUpSuccess(navData)
                Log.d("MyLog", "SendPass_2")
            },
            onSignUpFailure = { errorMassage ->
                errorState.value = errorMassage
            }
        )
    }

    // fun getAccountClosed(){
    fun getAccountState() {
        currentUser.value = authManager.getCurrentUser()
    }

    fun signOut() {
        authManager.signOut()
        currentUser.value = null
    }
}