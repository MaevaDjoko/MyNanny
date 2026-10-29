package com.example.mynanny.di

import com.example.mynanny.data.session.repository.SessionRepository
import com.example.mynanny.data.session.repository.SessionRepositoryImpl
import com.example.mynanny.data.signup.repository.SignUpRepository
import com.example.mynanny.data.signup.repository.SignUpRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindSignUpRepository(impl: SignUpRepositoryImpl): SignUpRepository
}
