package com.example.mynanny.di

import android.util.Log
import com.example.mynanny.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        val configured = BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_KEY.isNotBlank()
        check(configured || BuildConfig.DEBUG) { "supabase.url / supabase.key manquants dans local.properties" }
        if (!configured) {
            // Lets the UI run in debug without a backend; every network call will fail.
            Log.w("SupabaseModule", "Supabase non configuré : ajoute supabase.url et supabase.key dans local.properties")
        }
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL.ifBlank { "https://not-configured.supabase.co" },
            supabaseKey = BuildConfig.SUPABASE_KEY.ifBlank { "not-configured" },
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun provideAuth(client: SupabaseClient): Auth = client.auth

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient): Postgrest = client.postgrest
}
