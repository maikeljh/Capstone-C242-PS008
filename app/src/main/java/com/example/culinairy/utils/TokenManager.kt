package com.example.culinairy.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object TokenManager {
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val SECRET_KEY_ALIAS = "MySecretKey"
    private const val SHARED_PREF_NAME = "MySharedPreferences"
    private const val JWT_TOKEN_KEY = "jwt_token"
    private const val EXP_TOKEN = "exp"
    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")

    init {
        keyStore.load(null)
    }

    fun saveEncryptedToken(context: Context, token: String) {
        val encryptedToken = encryptToken(token)
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(JWT_TOKEN_KEY, encryptedToken).apply()
    }

    fun saveExpToken(context: Context, exp: Long) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(EXP_TOKEN, exp).apply()
    }

    // retrieve token from shared preferences
    fun retrieveToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val encryptedToken = sharedPreferences.getString(JWT_TOKEN_KEY, null)

        val decryptedToken = decryptToken(encryptedToken)
        if (decryptedToken != null) {
            Log.d("Token Utils - Retrieve Token", "Success to retrieve and decrypt JWT token")
            return decryptedToken
        } else {
            Log.e("Token Utils - Retrieve Token", "Failed to retrieve and decrypt JWT token")
        }

        return null
    }

    private fun encryptToken(token: String): String {

        // init cipher with AES/GCM/No padding
        val cipher = Cipher.getInstance(AES_MODE)

        // get secret key
        val secretKey = getOrCreateSecretKey()

        // init cipher in encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // generate iv and encrypt token
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(token.toByteArray())

        // combine iv and encrypted bytes into a single byte array
        val ivAndEncryptedBytes = ByteArray(iv.size + encryptedBytes.size)

        // copy iv bytes to the beginning of the combined array
        System.arraycopy(iv, 0, ivAndEncryptedBytes, 0, iv.size)

        // copy encrypted bytes to the combined array after the iv
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, iv.size, encryptedBytes.size)

        // encode to base64 and return as string
        return Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT)
    }

    // get or create secret key from android keystore
    private fun getOrCreateSecretKey(): SecretKey {
        val secretKeyEntry = keyStore.getEntry(SECRET_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        if (secretKeyEntry != null) {
            return secretKeyEntry.secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(SECRET_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    // decrypt token
    private fun decryptToken(encryptedToken: String?): String? {
        encryptedToken?.let {
            try {
                // init cipher with AES/GCM/No padding
                val cipher = Cipher.getInstance(AES_MODE)

                // get secret key
                val secretKey = getOrCreateSecretKey()

                // decode from base64
                val ivAndEncryptedBytes = Base64.decode(encryptedToken, Base64.DEFAULT)

                // separate iv and encrypted bytes
                val iv = ivAndEncryptedBytes.sliceArray(0 until 12)
                val encryptedBytes = ivAndEncryptedBytes.sliceArray(12 until ivAndEncryptedBytes.size)

                // init cipher in decrypt mode
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

                // decrypt the encrypted bytes with cipher
                val decryptedBytes = cipher.doFinal(encryptedBytes)

                // get the jwt token
                return String(decryptedBytes)
            } catch (e: Exception) {
                Log.e("Token Utils - Decrypt Token", "Error decrypting token: ${e.message}", e)
            }
        }
        return null
    }

    suspend fun checkToken(context: Context): Boolean {
        val jwtToken = retrieveToken(context)
        return if (jwtToken != null) {
            checkTokenExpiry(context, jwtToken)
        } else {
            LogoutManager.logout(context)
            false
        }
    }

    private suspend fun checkTokenExpiry(context: Context, jwtToken: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val tokenExpiry = sharedPreferences.getLong("exp", 0L)
        val currentTimeSeconds = System.currentTimeMillis() / 1000
        if (tokenExpiry < currentTimeSeconds) {
            LogoutManager.logout(context)
            return false
        } else {
            return true
        }
    }

    fun getExpToken(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(EXP_TOKEN, 0L)
    }

    fun clearTokens(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(JWT_TOKEN_KEY).apply()
        sharedPreferences.edit().remove(EXP_TOKEN).apply()
    }
}