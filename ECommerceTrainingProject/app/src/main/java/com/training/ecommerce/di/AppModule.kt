package com.training.ecommerce.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.training.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import com.training.ecommerce.data.models.user.UserDetailsModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun fakeUserData(): UserDetailsModel {
        return UserDetailsModel(
            id = "1236", email = "eslam@mail.com"
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesDataSource(context: Application): AppPreferencesDataSource {
        return AppPreferencesDataSource(context)
    }
}