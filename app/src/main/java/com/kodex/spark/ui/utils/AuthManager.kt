package com.kodex.spark.ui.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kodex.spark.ui.data.MainScreenDataObject
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth,
) {

    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignUpFailure("Email and Password be empty")
            Log.d("MyLog", "SendPass_3")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignUpSuccess(
                    MainScreenDataObject(
                    task.result.user?.uid!!,
                    task.result.user?.email!!
                )
                )
            }
            .addOnFailureListener() {
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignInFailure("Email and Password be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    onSignInSuccess(
                        MainScreenDataObject(
                            task.result.user?.uid!!,
                            task.result.user?.email!!
                        )
                    )
            }
            .addOnFailureListener() {
                onSignInFailure(it.message ?: "Ошибка входа")
            }
    }

    fun resetPassword(
        email: String,
        onResetPasswordSuccess: () -> Unit,
        onResetPasswordFailure: (String) -> Unit,
    ) {
        if (email.isEmpty()) {
            onResetPasswordFailure("Email: не должен быть пусым")
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResetPasswordSuccess
                 }
            }.addOnFailureListener{result->
                onResetPasswordFailure(result.message ?: "Ошибка восставноления пароля")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}

