package com.loopr.data.repository

import com.loopr.data.local.datastore.UserProfileDataStore
import com.loopr.data.local.datastore.Web3AuthSessionManager
import com.loopr.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDataStore: UserProfileDataStore,
    private val web3AuthSessionManager: Web3AuthSessionManager
) {

    fun getUserProfile(): Flow<UserProfile> {
        return userProfileDataStore.userProfile
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfileDataStore.saveUserProfile(userProfile)
    }

    suspend fun clearUserProfile() {
        userProfileDataStore.clearUserProfile()
    }

    fun hasUserProfile(): Flow<Boolean> {
        return userProfileDataStore.hasUserProfile()
    }

    // Web3Auth Session Management
    fun saveWeb3AuthSession(sessionData: String) {
        web3AuthSessionManager.saveSession(sessionData)
    }

    fun getWeb3AuthSession(): String? {
        return web3AuthSessionManager.getSession()
    }

    fun clearWeb3AuthSession() {
        web3AuthSessionManager.clearSession()
    }

    fun hasValidWeb3AuthSession(): Boolean {
        return web3AuthSessionManager.hasValidSession()
    }

    suspend fun logout() {
        clearUserProfile()
        clearWeb3AuthSession()
    }
}
