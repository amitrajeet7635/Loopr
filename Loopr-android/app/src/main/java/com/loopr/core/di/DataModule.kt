package com.loopr.core.di

import android.content.Context
import com.loopr.data.local.datastore.UserProfileDataStore
import com.loopr.data.local.datastore.Web3AuthSessionManager
import com.loopr.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideUserProfileDataStore(
        @ApplicationContext context: Context
    ): UserProfileDataStore {
        return UserProfileDataStore(context)
    }

    @Provides
    @Singleton
    fun provideWeb3AuthSessionManager(
        @ApplicationContext context: Context
    ): Web3AuthSessionManager {
        return Web3AuthSessionManager(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userProfileDataStore: UserProfileDataStore,
        web3AuthSessionManager: Web3AuthSessionManager
    ): UserRepository {
        return UserRepository(userProfileDataStore, web3AuthSessionManager)
    }
}
