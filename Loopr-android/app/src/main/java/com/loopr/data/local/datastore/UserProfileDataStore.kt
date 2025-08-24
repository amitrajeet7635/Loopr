package com.loopr.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.loopr.data.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

@Singleton
class UserProfileDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val EMAIL_ID_KEY = stringPreferencesKey("email_id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val PROFILE_IMAGE_URL_KEY = stringPreferencesKey("profile_image_url")
        private val WEB3AUTH_ID_KEY = stringPreferencesKey("web3auth_id")
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data
        .map { preferences ->
            UserProfile(
                emailId = preferences[EMAIL_ID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                profileImageUrl = preferences[PROFILE_IMAGE_URL_KEY] ?: "",
                web3AuthId = preferences[WEB3AUTH_ID_KEY] ?: ""
            )
        }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_ID_KEY] = userProfile.emailId
            preferences[NAME_KEY] = userProfile.name
            preferences[PROFILE_IMAGE_URL_KEY] = userProfile.profileImageUrl
            preferences[WEB3AUTH_ID_KEY] = userProfile.web3AuthId
        }
    }

    suspend fun clearUserProfile() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun hasUserProfile(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[WEB3AUTH_ID_KEY]?.isNotEmpty() == true
        }
    }
}
