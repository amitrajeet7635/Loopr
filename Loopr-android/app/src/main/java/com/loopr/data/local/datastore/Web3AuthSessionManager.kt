package com.loopr.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Web3AuthSessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val SESSION_PREFS_NAME = "web3auth_session_prefs"
        private const val SESSION_KEY = "web3auth_session"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            SESSION_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveSession(sessionData: String) {
        encryptedPrefs.edit()
            .putString(SESSION_KEY, sessionData)
            .apply()
    }

    fun getSession(): String? {
        return encryptedPrefs.getString(SESSION_KEY, null)
    }

    fun clearSession() {
        encryptedPrefs.edit()
            .remove(SESSION_KEY)
            .apply()
    }

    fun hasValidSession(): Boolean {
        return getSession() != null
    }
}
