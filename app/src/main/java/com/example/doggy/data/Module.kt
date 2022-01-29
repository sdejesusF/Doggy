package com.example.doggy.data

import android.app.Application
import com.example.doggy.AppInitializer
import com.example.doggy.data.local.DoggyDatabase
import com.example.doggy.data.network.DogApi
import com.example.doggy.data.sync.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val baseRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://api.thedogapi.com")
        .addConverterFactory(GsonConverterFactory.create())

    private val okHttpClientBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)

    @Provides
    @Singleton
    fun provideDogApi(): DogApi {
        return baseRetrofitBuilder
            .client(okHttpClientBuilder.build())
            .build()
            .create(DogApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreModule {
    @Binds
    @Singleton
    abstract fun provideDogStore(breedStore: BreedStoreDefault): BreedStore
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = DoggyDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideNotesDao(database: DoggyDatabase) = database.getBreedDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SynModule {
    @Singleton
    @Binds
    abstract fun provideSyncManager(syncManager: SyncOrchestratorDefault): SyncOrchestrator

    @Singleton
    @Binds
    abstract fun provideSyncer(syncer: SyncerDefault): Syncer

    @Binds
    @IntoSet
    abstract fun provideSyncInitializer(syncInitializer: SyncInitializer): AppInitializer
}