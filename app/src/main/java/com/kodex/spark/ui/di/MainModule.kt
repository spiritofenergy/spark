package com.kodex.spark.ui.di

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kodex.spark.ui.utils.AuthManager
import com.kodex.spark.ui.utils.FirestoreManager
import com.kodex.spark.ui.utils.store.StoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return Firebase.auth
    }
    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseManager(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): FirestoreManager{
        return FirestoreManager(auth,db)
    }
    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth
    ): AuthManager{
        return AuthManager(auth)
    }

    @Provides
    @Singleton
    fun provideStoreManager(
        app: Application
    ):StoreManager{
        return StoreManager(app)
    }
}
